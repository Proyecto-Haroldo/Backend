FROM ubuntu:latest
LABEL authors="samuc"

ENTRYPOINT ["top", "-b"]