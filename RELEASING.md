# Releasing

1. Make sure you're on the master branch.
2. Change `ext.libraryVersion` in root build.gradle.
3. Update CHANGELOG.md for the impending release.
4. `./gradlew clean assembleRelease && ./gradlew publishReleasePublicationToMavenCentralRepository`
5. Visit [Sonatype Nexus](https://oss.sonatype.org/#stagingRepositories). Verify the artifacts,
   close the staging repository, and release the closed staging repository.
6. Commit and push the release changes to master.
7. Create the release tag on GitHub.
