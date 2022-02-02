FROM node:16.13.2-alpine

RUN mkdir -p /src/jttracker
WORKDIR /usr/src/jttracker

RUN addgroup -S frontendgroup && adduser -S frontenduser -G frontendgroup -D

RUN apk update && apk upgrade
RUN apk add git

COPY . /usr/src/jttracker/
RUN npm install
RUN npm run build

USER frontenduser

EXPOSE 3000

ENV NUXT_HOST=0.0.0.0
ENV NUXT_PORT=3000
