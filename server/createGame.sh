#!/bin/sh

curl -H "Content-Type: application/json" -X POST -d @createGame.json http://localhost:8080/commands
