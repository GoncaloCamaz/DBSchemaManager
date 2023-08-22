FROM node:15 as build

WORKDIR /home/app/frontend

COPY . /home/app/frontend

COPY package*.json /home/app/frontend/

RUN npm install --no-audit
RUN npm run build

COPY . /home/app/frontend

FROM nginx:1.19.9
COPY --from=build /home/app/frontend/build /usr/share/nginx/html

RUN rm /etc/nginx/conf.d/default.conf

COPY nginx/nginx.conf /etc/nginx/conf.d

EXPOSE 81

CMD ["nginx", "-g", "daemon off;"]