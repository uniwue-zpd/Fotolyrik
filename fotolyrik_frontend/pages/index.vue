<script setup lang="ts">
// TODO: Define functions to parse data for the carousel and the bar chart

import { ref } from 'vue'

const images = ref([
    'k-1.jpg',
    'k-2.jpg',
    'k-3.jpg',
    'k-4.jpg',
    'k-5.jpg',
    'k-6.jpg',
    'k-7.jpg',
    'k-8.jpg',
    'k-9.jpg',
    'k-10.jpg'
]);

const topics = ref([
    't-1.jpg',
    't-2.jpg',
    't-3.jpg',
    't-4.jpg',
    't-5.jpg',
    't-6.jpg',
    't-7.jpg',
    't-8.jpg',
    't-9.jpg',
    't-10.jpg'
]);

const responsiveOptions = ref([
  {
    breakpoint: '1400px',
    numVisible: 2,
    numScroll: 1
  },
  {
    breakpoint: '1199px',
    numVisible: 3,
    numScroll: 1
  },
  {
    breakpoint: '767px',
    numVisible: 2,
    numScroll: 1
  },
  {
    breakpoint: '575px',
    numVisible: 1,
    numScroll: 1
  }
]);
</script>

<template>
  <div class="flex flex-col gap-4">
    <div class="carousel bg-[#F1F2F2] p-5">
      <Carousel :value="images" :numVisible="3" :numScroll="1" :responsiveOptions="responsiveOptions" circular :autoplayInterval="3000">
        <template #item="{ data }">
          <div class="p-2">
            <img :src="data" alt="image in carousel" class="w-full rounded shadow-md" />
          </div>
        </template>
      </Carousel>
    </div>
    <div class="intro p-5">
      <div class="px-10">
      <h1 class="text-3xl outfit-headline font-bold text-[#063D79]">
        Fotolyrik in illustrierten Zeitschriften zwischen 1895 und 1945
      </h1>
      </div>
      <div class="grid grid-cols-2 gap-x-10 gap-y-5 p-10">
        <div>
          Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod
          tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et
          accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est
          Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
          sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.
          At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata
          sanctus est Lorem ipsum dolor sit amet.
        </div>
        <div>
          Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod
          tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et
          accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est
          Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
          sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.
          At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata
          sanctus est Lorem ipsum dolor sit amet.
        </div>
        <div>
          Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod
          tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et
          accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est
          Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
          sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.
          At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata
          sanctus est Lorem ipsum dolor sit amet.
        </div>
        <div class="justify-self-center pt-5">
        <NuxtLink to="/project" class="text-xl border-2 border-solid border-spacing-2 border-[#F04E30] text-[#F04E30] averia-layout px-6 py-2">
        das Projekt
      </NuxtLink>
        </div>
      </div>
    </div>
    <div class="px-10">
      <h1 class="text-3xl outfit-headline font-bold text-[#063D79]">Sammlung</h1>
    </div>
    <div class="collection bg-[#F1F2F2] p-5">
      <div class="flex flex-col gap-2">
        <h2 class="text-2xl averia-layout italic text-[#063D79]">Highlight</h2>
        <div class="flex flex-row space-x-5">
          <div class="basis-1/3 bg-white font-bold">Text placeholder</div>
          <div class="basis-2/3 font-bold">
            <img src="../public/s-1.jpg"/>
          </div>
        </div>
      </div>
    </div>
    <div class="analytics p-5">
      <div class="flex flex-col gap-2">
        <h2 class="text-2xl averia-layout italic text-[#063D79]">Fotolyrik in Zahlen</h2>
        <div class="font-bold">
          Bar chart placeholder
          <i class="pi pi-chart-bar"/>
        </div>
        <div class="columns-2">
          <div>
            Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod
            tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et
            accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est
            Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
            sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.
            At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata
            sanctus est Lorem ipsum dolor sit amet.
          </div>
          <NuxtLink to="/collection" class="text-xl border-2 border-solid border-spacing-2 border-[#F04E30] text-[#F04E30] averia-layout px-6 py-2">
            zur Sammlung
          </NuxtLink>
        </div>
      </div>
    </div>
    <div class="topics bg-[#F1F2F2] p-5">
      <div class="flex flex-col gap-2">
        <h2 class="text-2xl averia-layout italic text-[#063D79]">Themen</h2>
        <Carousel :value="topics" :numVisible="4" :numScroll="1" :responsiveOptions="responsiveOptions" circular :autoplayInterval="3000">
          <template #item="{ data }">
            <div class="p-2">
              <img :src="data" alt="image in carousel" class="w-full rounded shadow-md" />
            </div>
          </template>
        </Carousel>
        <div class="flex flex-row justify-center">
          <NuxtLink to="/topics" class="outfit-headline text-[#F04E30] bg-white p-1">
            alle Themen
          </NuxtLink>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>
