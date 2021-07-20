/**
 * Global js variables and functions for all pages.
 * 
 * The idea is for this to be as minimal as possible. 
 * 
 * All my main functionality should be in /kame-house project
 * 
 * @author nbrest
 */
var countdownCounter = 60;

/** 
 * @deprecated
 * Refresh the page after the specified seconds. 
 * Simulating a loop by recursively calling the same function 
 */
function refreshPageLoop() {
  if (typeof countdownCounter == 'undefined') {
    countdownCounter = 60;
  }
  if (countdownCounter > 0) {
    document.getElementById('count').innerHTML = countdownCounter--;
    setTimeout(refreshPageLoop, 1000);
  } else {
    location.href = '/';
  }
}

/** Toggle expanding/collapsing the root menu hamburguer */
function toggleRootNav() {
  let rootMenu = document.getElementById("groot-menu");
  if (rootMenu.className === "groot-nav") {
    rootMenu.className += " responsive";
  } else {
    rootMenu.className = "groot-nav";
  }
}

/** Render the root menu */
function renderRootMenu() {
  $("#groot-menu-wrapper").load("/kame-house-groot/html-snippets/groot-menu.html", () => {
    updateGRootMenuActiveTab();
  });
}

/** Get session status from the backend */
function getSessionStatus(successCallback, errorCallback) {
  const SESSION_STATUS_API = '/kame-house-groot/api/v1/commons/session/status.php';
  httpClient.get(SESSION_STATUS_API, null,
    (responseBody, responseCode, responseDescription) => successCallback(responseBody, responseCode, responseDescription),
    (responseBody, responseCode, responseDescription) => errorCallback(responseBody, responseCode, responseDescription));
}

/**
 * Open the tab specified by its id.
 */
 function openTab(selectedTabDivId, cookiePrefix) {
  // Set current-tab cookie
  cookiesUtils.setCookie(cookiePrefix + '-current-tab', selectedTabDivId);
  
  // Update tab links
  let tabLinks = document.getElementsByClassName("tab-groot-link");
  for (let i = 0; i < tabLinks.length; i++) {
    tabLinks[i].className = tabLinks[i].className.replace(" active", "");
  }
  let selectedTabLink = document.getElementById(selectedTabDivId + '-link');
  selectedTabLink.classList.add("active");

  // Update tab content visibility
  let kamehouseTabContent = document.getElementsByClassName("tab-groot-content");
  for (let i = 0; i < kamehouseTabContent.length; i++) {
    kamehouseTabContent[i].style.display = "none";
  }
  let selectedTabDiv = document.getElementById(selectedTabDivId);
  selectedTabDiv.style.display = "block";
}

/**
* Set active tab in the groot sub menu.
*/
function updateGRootMenuActiveTab() {
  let pageUrl = window.location.pathname;
  $("#groot-menu a").toArray().forEach((navItem) => {
    $(navItem).removeClass("active");
    if (pageUrl.startsWith("/kame-house-groot/admin/server-manager")) {
      if ($(navItem).attr("id") == "nav-server-manager") {
        $(navItem).addClass("active");
      }
    }
    if (pageUrl.startsWith("/kame-house-groot/admin/my-scripts")) {
      if ($(navItem).attr("id") == "nav-my-scripts") {
        $(navItem).addClass("active");
      }
    }
  });
}
