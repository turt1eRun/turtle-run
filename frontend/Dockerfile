# Build Stage
FROM node:20-alpine AS build

WORKDIR /app

COPY package.json yarn.lock ./
RUN yarn install --frozen-lockfile

COPY . .
RUN yarn build

# RUN Stage
FROM nginx:alpine
COPY ./nginx/default.prod.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/out /var/www/html/turtlerun
EXPOSE 80

ENTRYPOINT ["nginx", "-g", "daemon off;"]
