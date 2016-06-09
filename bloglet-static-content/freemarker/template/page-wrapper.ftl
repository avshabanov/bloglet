<#macro page title>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8" />

    <meta name="Author" content="Alex" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />

    <title>Bloglet &raquo; ${title}</title>

    <link rel="stylesheet" type="text/css" href="/libs/bootstrap/css/bootstrap.css" />
    <link rel="stylesheet" type="text/css" href="/libs/bootstrap/css/bootstrap-theme.css" />
    <link rel="stylesheet" type="text/css" href="/css/global.css" />
  </head>
  <body>

  <#-- Navigation -->
  <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
      <div class="navbar-header"> <#-- Brand and toggle get grouped for better mobile display -->
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#g-app-navbar-collapse">
          <span class="sr-only">Toggle navigation</span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="/g/index">Bloglet</a>
      </div>
      <div class="collapse navbar-collapse" id="g-app-navbar-collapse"> <#-- Collect the nav content for toggling -->
        <ul class="nav navbar-nav">
          <li><a href="/g/index">Catalog</a></li>
          <li class="nav-divider"></li>
          <li><a href="/g/about">About</a></li>
          <li class="nav-divider"></li>
          <li><a href="/g/logout">Logout</a></li>
        </ul>
      </div> <#-- /.navbar-collapse -->
    </div> <#-- /.container -->
  </nav> <#-- /nav -->

  <div id="main-content" class="container">
    <#nested/>
  </div>

  <div id="footer">
  </div>

  <script type="text/javascript" src="/js/bundle.js"></script>
</body>
</html>
</#macro>

<#macro heading>
<!-- TODO: Heading -->
</#macro>
