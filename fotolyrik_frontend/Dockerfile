FROM node:lts-alpine

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

EXPOSE 3000

ENV NODE_OPTIONS="--max-old-space-size=4096"
RUN npm run build

CMD ["npm", "run", "start"]
