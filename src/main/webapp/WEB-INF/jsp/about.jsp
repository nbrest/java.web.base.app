<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width">
<meta name="description" content="kame-house application">
<meta name="keywords" content="kame-house nicobrest">
<meta name="author" content="nbrest">

<title>kameHouse - About</title>
<link rel="icon" type="img/ico" href="img/favicon.ico" />
<link rel="stylesheet" href="lib/css/bootstrap.min.css" />
<link rel="stylesheet" href="css/general.css" />
<link rel="stylesheet" href="css/header.css" />
<link rel="stylesheet" href="css/footer.css" />
<link rel="stylesheet" href="css/about.css" />
</head>
<body>
  <div id="headerContainer"></div>
  <div class="main-body">
    <div class="default-layout">
      <div id="main-article">
        <h3 class="h3-kh txt-l-kh">About Us</h3>
        <p>KameHouse is a project I started mainly to practice Java and frontend development.
        The main functionality of this application is around managing media files. I use VLC player
        to play movies, series, anime. So I developed this application to remotely control the
        computer on which VLC is running, which is attached to a TV, from either my phone 
        or another computer.</p>
        <p class="bg-lighter-kh pd-15-kh">This application has other functionality as well. I added the
        functionality to remotely lock, unlock, wake up screen and shutdown the computer, among
        others. You can find the full description of the project in the github link below.</p>
        <p>KameHouse is an open source project. You can find the code for this project at: 
        <a href="https://github.com/nbrest/java.web.kamehouse" target="_blank" class="btn btn-outline-danger btn-borderless">
        <img src="./img/social-media/github.png" class="social-media-logo"/> github.com/nbrest/java.web.kamehouse</a>
        </p>
      </div>
      <div id="sidebar">
        <div class="bg-lighter-kh pd-15-kh">
          <h3>Who are we</h3>
          <p>I'm Nicolas Brest. I'm a software engineer and Java backend developer from Argentina,
           currently living in Melbourne :)</p>
          <p id="social-media-links" class="bg-default-kh pd-15-kh">You can find me on: 
          <br><a href="https://www.linkedin.com/in/nicolasbrest" target="_blank" class="btn btn-outline-info btn-borderless"><img src="./img/social-media/linkedin.png" class="social-media-logo"/> Linkedin</a>
          <br><a href="https://github.com/nbrest" target="_blank" class="btn btn-outline-info btn-borderless"><img src="./img/social-media/github.png" class="social-media-logo"/> Github</a>
          <br><a href="https://www.facebook.com/brest.nico" target="_blank" class="btn btn-outline-info btn-borderless"><img src="./img/social-media/facebook.png" class="social-media-logo"/> Facebook</a>
          </p>
        </div>
      </div>
    </div>
  </div>
  <div id="about-newsletter-wrapper">
    <div id="newsletter"></div>
  </div>
  <div id="footerContainer"></div>
  <script src="lib/js/jquery-2.0.3.min.js"></script>
  <script src="js/general.js"></script>
  <script src="js/importHeaderFooter.js"></script>
</body>
</html>
