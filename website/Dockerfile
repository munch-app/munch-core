FROM node:8.11.0-alpine

# Create app directory
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

# Install app dependencies
RUN apk update && apk upgrade && apk add git

COPY package.json /usr/src/app/package.json
RUN npm install

COPY . /usr/src/app/

ARG deployment
RUN if [ "$deployment" = "staging" ] ; then echo "Disallow: /" >> /usr/src/app/static/robots.txt ; fi

ARG url
ENV API_URL_BROWSER $url

ENV HOST 0.0.0.0
ENV NODE_ENV production

# Build app
RUN npm run build
EXPOSE 3000

# start command
CMD [ "npm", "start" ]
