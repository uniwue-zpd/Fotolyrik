# Environment for the Fotolyrik Project

---
This repository contains the code for the online instance of **Fotolyrik Project**. 
The project is divided into two parts: the `fotolyrik_backend` and the `fotolyrik_frontend`.

**Readme files:**
- [`backend`](./fotolyrik_backend/README_backend.md)
- [`frontend`](./fotolyrik_frontend/README_frontend.md)

## Table of contents
- [Introduction](#introduction)
- [Getting started](#getting-started)
- [Launching with Docker Compose](#launching-with-docker-compose)

## Introduction

## Getting started

## Launching with Docker Compose

> [!NOTE]
> Make sure you have the Docker on your own computer

1. Copy the file [`template.application.properties`](./fotolyrik_backend/src/main/resources/template.application.properties)
and save it as `application_properties`. 
Afterward, fill out the file with the relevant information about the database.
2. Copy the file [`template.env`](./template.env) and save it as `.env`.
Afterward, Fill out the marked fields with the same access data as in `application.properties`
3. If you have a dump of the database for the project:
- Place the dump file in the root folder of the project
- Open the [`docker-compose.yml`](./docker-compose.yml), uncomment the following line and
set the name of the dump file instead of `<your_dump>`:
```yaml
# - ./<your_dump>.sql:/docker-entrypoint-initdb.d/<your_dump>.sql
```
4. Open the terminal and execute the following command from the root folder:
```shell
docker-compose build --no-cache
```
5. Wait until the project is built and then run the container (detached mode):
```shell
docker-compose up -d
```
Now you can see the project in the Docker app.
Click on the button for the 80th port to start the web app in your browser.
