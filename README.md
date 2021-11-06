# ElatsicVitae
## BY GUELIANE Belkacem AND LEE Jae-Soo
A Hiring Master App based on Elasticsearch

# Getting started

The App requires [Elasticsearch](https://www.elastic.co/fr/downloads/elasticsearch) and [Logstash](https://www.elastic.co/fr/downloads/logstash) as well as [Kibana](https://www.elastic.co/fr/downloads/kibana) (Optional for visualizing data)

To makes things simpler, you can run them on Docker using the provided config file, run the following command and make sure that you are in the **/ElasticVitae** directory, also check the presence of the **/ElasticVitae/logstash/logstash.conf**

```
docker-compose up -d
```

Next, you'll need to start the Maven/SpringBoot backend by running the following in the **clean** directory

```
java -jar ./base-0.0.1-SNAPSHOT.jar
```

\*\*

Test using any http client (ex [Postman](https://www.postman.com/downloads/))
The App will run by default on localhost:8080, Elasticsearch on localhost:9200, Kibana on localhost:5601, and Logstash runs on localhost:9600 (and listens on localhost:8089)

# Features

## API

ElasticVitae allows to store profiles (eventually potential job candidates), as well as run any number of custom queries to filter for target candidates.

The API can be broken down as such:

- **Creating a new profile (POST)**

/api/cv/new

**exemple**

```json
{
  "name": "Belkacem GUELIANE",
  "email": "mohamed.gueliane@gmail.com",
  "phone": "0753556764",
  "cvsource": "changeme!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
}
```
**don't forget to change the cvsource in the body**


- **Gat ALL Profiles (GET)**

/api/cv/search/all

- **Search by ID (GET)**

/api/cv/search/id={id}

**exemple**

/api/cv/search/id=b70094c8-694a-4791-a910-d3858272a213

gives:

```json
{
  "name": "Belkacem GUELIANE",
  "email": "mohamed.gueliane@gmail.com",
  "phone": "0753556764",
  "cvsource": "C:/Users/moham/Desktop/DAAR/project2/garbage/cvbelkacem.pdf",
  "id": "b70094c8-694a-4791-a910-d3858272a213",
  "cvdata": [
    "Expériences ",
    "professionnelles",
    "PROJETS",
    "COMPÉTENCES",
    "C#",
    "JAVA",
    "C/C++",
    "JavaScript/React",
    "Python",
    "MATLAB",
    "Français ",
    "Anglais",
    "..."
  ]
}
```

- **Search by Term (GET)**

/api/cv/search/term={term}"

It searches for all candidates with profiles that contain a **searchTerm** in any field
**exemple**

/api/cv/search/term=java"

gives:

```json
{
  "name": "Belkacem GUELIANE",
  "email": "mohamed.gueliane@gmail.com",
  "phone": "0753556764",
  "cvsource": "C:/Users/moham/Desktop/DAAR/project2/garbage/cvbelkacem.pdf",
  "id": "b70094c8-694a-4791-a910-d3858272a213",
  "cvdata": [
    "Expériences ",
    "professionnelles",
    "PROJETS",
    "COMPÉTENCES",
    "C#",
    "JAVA",
    "C/C++",
    "JavaScript/React",
    "Python",
    "MATLAB",
    "Français ",
    "Anglais",
    "..."
  ]
}
```

- **Search by Term In CV (GET)**

/api/cv/search/cv/term=java"

It searches for all candidates with profiles that contain a **searchTerm** in **cvdata\*\*** field
**exemple**

/api/cv/search/cv/term=java"

gives:

```json
{
  "name": "Belkacem GUELIANE",
  "email": "mohamed.gueliane@gmail.com",
  "phone": "0753556764",
  "cvsource": "C:/Users/moham/Desktop/DAAR/project2/garbage/cvbelkacem.pdf",
  "id": "b70094c8-694a-4791-a910-d3858272a213",
  "cvdata": [
    "Expériences ",
    "professionnelles",
    "PROJETS",
    "COMPÉTENCES",
    "C#",
    "JAVA",
    "C/C++",
    "JavaScript/React",
    "Python",
    "MATLAB",
    "Français ",
    "Anglais",
    "..."
  ]
}
```

- **MultiSearch (POST)**

/api/cv/multisearch

It searches for all candidates with profiles that contain **all search terms** in any field.
**exemple**

/api/cv/multisearch

```json
["java", "c++"]
```

gives:

```json
{
  "name": "Belkacem GUELIANE",
  "email": "mohamed.gueliane@gmail.com",
  "phone": "0753556764",
  "cvsource": "C:/Users/moham/Desktop/DAAR/project2/garbage/cvbelkacem.pdf",
  "id": "b70094c8-694a-4791-a910-d3858272a213",
  "cvdata": [
    "Expériences ",
    "professionnelles",
    "PROJETS",
    "COMPÉTENCES",
    "C#",
    "JAVA",
    "C/C++",
    "JavaScript/React",
    "Python",
    "MATLAB",
    "Français ",
    "Anglais",
    "..."
  ]
}
```

- **Custom Search (POST)**

/api/cv/search

It searches for all candidates with profiles that contain a **searchTerm** in the specified **fields**
**exemple**

/api/cv/search

```json
{
  "fields": ["cvdata"],
  "searchTerm": "java"
}
```

gives:

```json
{
  "name": "Belkacem GUELIANE",
  "email": "mohamed.gueliane@gmail.com",
  "phone": "0753556764",
  "cvsource": "C:/Users/moham/Desktop/DAAR/project2/garbage/cvbelkacem.pdf",
  "id": "b70094c8-694a-4791-a910-d3858272a213",
  "cvdata": [
    "Expériences ",
    "professionnelles",
    "PROJETS",
    "COMPÉTENCES",
    "C#",
    "JAVA",
    "C/C++",
    "JavaScript/React",
    "Python",
    "MATLAB",
    "Français ",
    "Anglais",
    "..."
  ]
}
```

- **Remove ALL (DELETE)**

/api/cv/remove/all

It empties the candidate list

- **Remove by ID (DELETE)**

/api/cv/remove/id={id}

**exemple**

/api/cv/remove/id=b70094c8-694a-4791-a910-d3858272a213

## LOGS

The App contains two profile modes:

- **dev** that logs to the Console
- **prod**
  this mode will log into **localhost:8089**, on which Logstash has a listener, the logs are the automatically collected by Logstash and can be analyzed on Kibana

The App **runs by default in dev mode**, for switwhing to the prod mode you can simply copy the **profile_prod/application.properties** and paste it in the same folder as the jar, this will automatically change the config and the App will start logging into logstash

By default, the App is in dev mode, by switching to prod by default it logs into logstash docker listening on port 8089.

Please use the CVs that we provided to run an example

(POST) on /api/cv/new
body:

```json
{
  "name": "Belkacem GUELIANE",
  "email": "mohamed.gueliane@gmail.com",
  "phone": "0753556764",
  "cvsource": "https://cv-example-website/belkacems-cv.pdf"
}
```

## Remarques

- **The cvsource can be either a web link or a local path**, ElasticVitae would load the document and store it, the source had to end either in **.pdf**, **.doc** or **.docx**

- Since FileBeat is not included in the docker config, it's necessary to either add it, or to run Logstash independently using the config in the **/clean/logstashlocal** folder (by adding it into the **/bin** folder in your logstash installation), if you choose to do this then it is necessary to switch the **log4j.properties** inside the jar for the alternative one in **/clean/logstashlocal**, as well as to specify the absolute path for the log file **logs/logs.log** in logstash.conf (replace "changeme"), this will allow the app to log in a local file in prod mode and hence Logstash can show the logs as they're being produced, examples of all modes of logging are shown in **/clean/img**.

- The App is vulnurable to **Man-in-the-Middle** attacks, caused by the disabeling of certain limitaions to simplify the setup process for users
