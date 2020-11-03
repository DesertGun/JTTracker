FROM node:12.18.3-alpine

RUN mkdir -p /src/jttracker
WORKDIR /usr/src/jttracker

RUN apk update && apk upgrade
RUN apk add git

COPY . /usr/src/jttracker/
RUN npm install
RUN npm run build

EXPOSE 3000

ENV NUXT_HOST=0.0.0.0
ENV NUXT_PORT=3000
