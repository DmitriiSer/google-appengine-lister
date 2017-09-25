google-appengine-lister
=======================

[appengine]: https://cloud.google.com/appengine/
[gcloud]: https://cloud.google.com/
[applink]: https://hybrid-shelter-180902.appspot.com/
[versions-plugin]: http://www.mojohaus.org/versions-maven-plugin/

This is the Lister project migrated to Google Cloud Platform App Engine.

See the [Google App Engine standard environment documentation][appengine] for more
detailed instructions.

## [Demo under construction][applink] (Deployed using [Google Cloud Platform][gcloud])

## Maven
### Running locally

    mvn appengine:devserver

### Deploying

    mvn appengine:update

## Testing

    mvn verify

As you add / modify the source code (`src/main/java/...`) it's very useful to add
[unit testing](https://cloud.google.com/appengine/docs/java/tools/localunittesting)
to (`src/main/test/...`).  The following resources are quite useful:

* [Junit4](http://junit.org/junit4/)
* [Mockito](http://mockito.org/)
* [Truth](http://google.github.io/truth/)

## Updating to latest Artifacts

An easy way to keep your projects up to date is to use the maven [Versions plugin][versions-plugin].

    mvn versions:display-plugin-updates
    mvn versions:display-dependency-updates
    mvn versions:use-latest-versions

Note - Be careful when changing `javax.servlet` as App Engine Standard uses 3.1 for Java 8, and 2.5
for Java 7.

Our usual process is to test, update the versions, then test again before committing back.

