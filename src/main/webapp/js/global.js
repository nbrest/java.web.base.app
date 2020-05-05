/**
 * Global js variables and functions for all pages.
 * 
 * Dependencies: logger, httpClient.
 * 
 * @author nbrest
 */
/** 
 * ----- Global variables ------------------------------------------------------------------ 
 */
var global = {};
global.session = {};

/** 
 * Object that determines which module is loaded. 
 * For example, when timeUtils gets loaded, set modules.timeUtils = true;
 * I use it in waitForModules() to check if a module is loaded or not.
 */
var modules = {};

/** Global modules */
var timeUtils;
var httpClient;
var logger;

/** 
 * ----- Global functions ------------------------------------------------------------------
 */
function main() {
  loadModules();
  var loadingModules = ["logger", "httpClient"];
  waitForModules(loadingModules, initGlobal);
}

/** 
 * Waits until all specified modules in the moduleNames array are loaded, 
 * then executes the specified init function.
 * Use this function in the main() of each page that requires modules like logger and httpClient
 * to be loaded before the main code is executed.
 */
async function waitForModules(moduleNames, initFunction) {
  //console.log("init: " + initFunction.name + ". Start waitForModules " + JSON.stringify(moduleNames) + ". modules status: " + JSON.stringify(modules));
  var areAllModulesLoaded = false;
  while (!areAllModulesLoaded) {
    //console.log("init: " + initFunction.name + ". Waiting waitForModules " + JSON.stringify(moduleNames) + ". modules status: " + JSON.stringify(modules));
    var isAnyModuleStillLoading = false;
    moduleNames.forEach(function (moduleName) {
      if (!modules[moduleName]) {
        isAnyModuleStillLoading = true;
      } 
    });
    if (!isAnyModuleStillLoading) {
      areAllModulesLoaded = true;
    }
    await sleep(3);
  }
  //console.log("init: " + initFunction.name + ". *** Finished *** waitForModules " + JSON.stringify(moduleNames) + ". modules status: " + JSON.stringify(modules));
  if (isFunction(initFunction)) {
    //console.log("Executing " + initFunction.name);
    initFunction();
  } 
}

/** Init function to call after all global dependencies are loaded */
function initGlobal() {
  logger.info("Started initializing global functions");
  loadHeaderAndFooter();
  //testLogLevel();
}

/** Load default modules. */
function loadModules() {
  loadTimeUtils();
  loadLogger();
  loadHttpClient();
}

/** Load time utils. */
function loadTimeUtils() {
  $.getScript("/kame-house/js/utils/time-utils.js", function (data, textStatus, jqxhr) {
    modules.timeUtils = true;
    timeUtils = new TimeUtils();
  });
}

/** Load logger object. */
function loadLogger() {
  $.getScript("/kame-house/js/utils/logger.js", function (data, textStatus, jqxhr) {
    waitForModules(["timeUtils"], function initLoggerModule() {
      modules.logger = true;
      logger = new Logger();
    });
  });
}

/** Load httpClient. */
function loadHttpClient() {
  $.getScript("/kame-house/js/utils/http-client.js", function (data, textStatus, jqxhr) {
    waitForModules(["logger"], function initHttpClientModule() {
      modules.httpClient = true;
      httpClient = new HttpClient();
    });
  });
}

/** Load header and footer. */
function loadHeaderAndFooter() {
  $.getScript("/kame-house/js/header-footer/header-footer.js", function (data, textStatus, jqxhr) {
    renderHeaderAndFooter();
  });
}

/** Test the different log levels. */
function testLogLevel() {
  console.log("logger.logLevel " + logger.logLevel);
  logger.error("This is an ERROR message");
  logger.warn("This is a WARN message");
  logger.info("This is an INFO message");
  logger.debug("This is a DEBUG message");
  logger.trace("This is a TRACE message");
}

/**
 * Sleep the specified milliseconds.
 * This function needs to be called in an async method, with the await prefix. 
 * Example: await sleep(1000);
 */
function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

/** Checks if a variable is undefined or null, an empty array [] or an empty object {}. */
function isEmpty(val) {
  let isNullOrUndefined = val === undefined || val == null;
  let isEmptyArray = !isNullOrUndefined && Array.isArray(val) && val.length <= 0;
  let isEmptyObject = !isNullOrUndefined && Object.entries(val).length === 0 && val.constructor === Object;
  return isNullOrUndefined || isEmptyArray || isEmptyObject;
}

/** Returns true if the parameter variable is a fuction. */
function isFunction(expectedFunction) {
  return expectedFunction instanceof Function;
}

/** Scroll to the top of the screen. */
function scrollToTop() {
  $('html, body').animate({
    scrollTop: 0
  }, '10');
}

/** Call main. */
$(document).ready(main);