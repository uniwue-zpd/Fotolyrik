FROM node:lts-alpine AS build

WORKDIR /app

COPY package*.json /app

RUN npm ci && npm cache clean --force

COPY . .

ENV NODE_OPTIONS="--max-old-space-size=4096"
RUN npm run build

FROM node:lts-alpine

WORKDIR /app

COPY --from=build /app/.output /app/.output

ENTRYPOINT ["node", ".output/server/index.mjs"]
