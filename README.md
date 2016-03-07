# mongeez-maven-plugin
Maven plugin for MongoDB migrations

#### Current version: 0.9.0-SNAPSHOT

### Usage

1. Add snapshot repository to your POM

    ```
    <pluginRepositories>
        <pluginRepository>
            <id>coderion-public-snapshots</id>
            <name>Coderion Public Snapshots</name>
            <url>http://nexus.coderion.pl/nexus/content/repositories/snapshots</url>
            <releases>
                <enabled>false</enabled>
            </releases>
        </pluginRepository>
    </pluginRepositories>
    ```

2. Add build plugin to your POM

    ```
    <build>
        <plugins>
            <plugin>
                <groupId>pl.coderion.mongodb</groupId>
                <artifactId>mongeez-maven-plugin</artifactId>
                <version>0.9.0-SNAPSHOT</version>
                <configuration>
                    <changeLogFile>src/main/mongeez/mongeez.xml</changeLogFile>
                    <propertyFile>src/main/mongeez/config.properties</propertyFile>
                </configuration>
            </plugin>
        </plugins>
    </build>
    ```

3. Run maven goal:

    ```
    mvn mongeez:update
    ```

### Configuration

TODO