
# How to override properties to simplify development

Create a copy of core.properties and add the following:

```
blogletWebsite.web.freemarkerPath=file:/PATH-TO/bloglet/bloglet-static-content/freemarker
brikar.dev.overrideStaticPath=/PATH-TO/bloglet/bloglet-static-content/target/release/blogletWebsite/web/static

brikar.settings.gracefulShutdownMillis=100
```

Then add VM property: ``-Dbrikar.settings.path=file:/PATH-TO-YOUR/bloglet-website.properties`` pointing to your custom properties.
