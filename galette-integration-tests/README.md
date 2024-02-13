## Tips

To debug a single test case, use the `verify` Maven phase.
For example, if you wanted to run and debug only `AssignmentITCase`, you would run:

```bash
mvn -Dmaven.failsafe.debug -Dit.test=AssignmentITCase verify
```

If an instrumented Java installation has already been created, and you want to run a minimal number of plugins when
running a test, you have to invoke the maven-dependency-plugin before failsafe.
For example,
```bash
mvn -Dit.test=AssignmentITCase dependency:properties failsafe:integration-test
```

Add the option `-Dgalette.forceCreation` to Maven commands to force the Galette Maven plugin to create a
new instrumented Java installation.
This will also delete associated caches of dynamically instrumented classes.