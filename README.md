Bloglet
=======

Simple blog web application that uses ``Tupl`` as database over ``tupl-support``.

# How to build?

Build requires ``maven ver. >= 3.2.0`` and ``nodejs ver. >= 5.0.0`` installed.

Once you have both maven and nodejs installed, run: ``mvn clean install`` in repository root.

# How to start?

Once project and its submodules are built (see previous step), go to ``bloglet-website`` and type:

```
mvn exec:java
```

Once you see port 8080 in your console output, go to browser and open ``http://127.0.0.1:8080``

You should see login page, enter ``testonly`` for username and ``test`` for password.

Once you logged in, you should see catalog page. Click on ``Load More`` button to get to older blog posts.

NOTE: this is still work in progress.

# How Tupl Is Used Here?

Note the usages of the following classes:

* DemoInitializer class that adds demo data to the Tupl DB in dev mode.
* BlogService interface, its usages and its implementation.
* BlogServiceTest - tests for BlogService.
