# Build Stage
FROM node:18 AS build

WORKDIR /app

COPY ./package*.json .
RUN npm install

COPY . .
RUN npm run build

# RUN Stage
FROM nginx:latest
COPY --from=build /app/out /var/www/html/turtlerun
COPY ./nginx/default.prod.conf /etc/nginx/conf.d/default.conf
EXPOSE 80

ENTRYPOINT ["nginx", "-g", "daemon off;"]
