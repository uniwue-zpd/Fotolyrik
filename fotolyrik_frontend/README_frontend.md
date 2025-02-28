# Frontend instance for the Fotolyrik project

> Have [Node.js](https://nodejs.org/en) and `npm` installed

### Running the project

> First of all, please make sure that you are in the [fotolyrik_frontend](../fotolyrik_frontend) directory.

#### 1. Install required packages

```shell
npm install
```

---

#### 2. Development server

To run the development version of the project please run following command:

```shell
npm run dev
```

- You can access the project via `http://localhost:3000`.

#### 3. Production-ready build

You can also create the ready-to-use frontend build:

```shell
npm run build
```

### Contribution guidelines

#### 1. Naming conventions

* For the pages: `kebab-case`. Example: `data-protection.vue`
* For the components: `PascalCase`. Example: `AppHeader.vue`

#### 2. Creating vue components

The code is based on [`Typescript`](https://www.typescriptlang.org/). Please use also `<script setup lang="ts">` when creating new vue components.

Each component has the following structure:

```vue
<script setup lang="ts">
    // code
</script>

<template>
    <!-- html markup with tailwind inline styling -->
    <p class="text-2xl text-white">Hello world</p>
</template>

<style scoped>
    /* CSS stylesheet (optional) */
</style>
```

#### 3. Folder structure and routing

Due to the usage of [Nuxt](https://nuxt.com/) framework we have the following folder structure for all components
and pages:

> root: `fotolyrik_frontend/`

```
app.vue           // main component which contains the header, main content slot and the footer

-| components/    // The folder for the components which should appear in every component
--| AppHeader.vue
--| AppFooter.vue
--| MainContent.vue

-| pages/
--| index.vue    // creates the home page with the '/' path
--| faq.vue
--| impressum.vue
--| persons/
---| index.vue   // creates the page with the '/persons' path
---| [id].vue    // allows generating pages for the person items with the '/persons/{id}' path
--| places/
---| index.vue
---| [id].vue

etc.
```
Accordingly, you don't need to create routes manually. `Nuxt` will generate them automatically based
on the folder structure.
