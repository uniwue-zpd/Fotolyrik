<script setup lang="ts">
import CollectionOverview from "~/components/UI/homepage/CollectionOverview.vue";

useHead(() => ({
  title: 'Fotolyrik - Startseite'
}));

import { ref } from 'vue';
import "maplibre-gl/dist/maplibre-gl.css"

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
  <div class="flex flex-col gap-5">
    <div class="carousel bg-[#F1F2F2] md:p-5">
      <Carousel :value="images" :numVisible="3" :numScroll="1" :responsiveOptions="responsiveOptions" circular :autoplayInterval="3000">
        <template #item="{ data }">
          <div class="p-4">
            <img :src="data" alt="image in carousel" class="w-full" />
          </div>
        </template>
      </Carousel>
    </div>
    <div class="intro py-10 px-6 md:px-15">
      <h1 class="text-3xl outfit-headline font-bold text-[#063D79]">
        Fotolyrik in illustrierten Zeitschriften zwischen 1895 und 1945
      </h1>
    </div>
    <div class="intro px-6 md:px-15">
      <div class="md:columns-2 space-y-8 gap-x-10 text-justify">
        <div>
          1 Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod
          tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et
          accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est
          Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
          sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.
          At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata
          sanctus est Lorem ipsum dolor sit amet.
        </div>
        <div>
          2 Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod
          tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et
          accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est
          Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
          sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.
          At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata
          sanctus est Lorem ipsum dolor sit amet.
        </div>
        <div>
          3 Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod
          tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et
          accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est
          Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
          sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.
          At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata
          sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
          sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.
          At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata
          sanctus est Lorem ipsum dolor sit amet.
        </div>
        <div class="justify-self-center pt-5 order-last">
        <NuxtLink to="/project" class="text-xl border-2 border-solid border-spacing-2 border-[#F04E30] text-[#F04E30] averia-layout px-6 py-2">
        das Projekt
      </NuxtLink>
        </div>
      </div>
    </div>
    <div class="py-8 px-6 md:px-15">
      <h1 class="text-3xl outfit-headline font-bold text-[#063D79]">Sammlung</h1>
    </div>
    <div class="collection bg-[#F1F2F2]">
      <div class="flex flex-col gap-2 px-6 md:px-15 py-10 md:py-14">
        <div class="pb-3">
          <h2 class="text-3xl averia-layout italic text-[#063D79]">Highlight</h2>
        </div>
        <div class="flex flex-col md:flex-row md:space-x-5">
          <div class="md:basis-1/4 bg-white font-bold">Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod
            tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et
            accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est
            Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr</div>
          <div class="pt-6 md:pt-0 md:basis-3/4">
            <img alt="placeholder image" src="../public/s-1.jpg"/>
          </div>
        </div>
      </div>
    </div>
    <div class="title py-8 pt-7 px-6 md:px-15">
      <h2 class="text-3xl outfit-headline font-bold text-[#063D79]">Fotolyrik in Zahlen</h2>
    </div>
    <div class="analytics px-6 md:px-15">
      <div class="flex flex-col gap-2">
        <CollectionOverview/>
        <div class="py-8">
        <div class="md:columns-2 space-y-8 gap-x-10 text-justify">
          <div>
            Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod
            tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et
            accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est
            Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
            sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.
            At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata
            sanctus est Lorem ipsum dolor sit amet.
          </div>
          <div class="justify-self-center pt-5 order-last">
          <NuxtLink to="/collection" class="text-xl border-2 border-solid border-spacing-2 border-[#F04E30] text-[#F04E30] averia-layout px-6 py-2">
            zur Sammlung
          </NuxtLink>
          </div>
        </div>
        </div>
      </div>
    </div>
    <div class="title pt-10 md:pt-6 pb-4 px-6 md:px-15">
      <h2 class="text-3xl outfit-headline font-bold text-[#063D79]">Themen</h2>
    </div>
    <div class="topics bg-[#F1F2F2] p-5">
      <div class="flex flex-col gap-2">
        <Carousel :value="topics" :numVisible="4" :numScroll="1" controls="true" indicator-type="disc" :responsiveOptions="responsiveOptions" circular :autoplayInterval="3000">
          <template #item="{ data }">
            <div class="p-2">
              <img :src="data" alt="image in carousel" class="w-full rounded-sm shadow-md" />
            </div>
          </template>
        </Carousel>
        <div class="flex flex-row justify-center">
          <NuxtLink to="/topics" class="inline-block px-8 py-2 outfit-headline font-semibold text-[#F04E30] bg-white p-1">
            alle Themen
          </NuxtLink>
        </div>
      </div>
    </div>
  </div>
</template>

<style>

.p-carousel {
  --p-carousel-indicator-width: 12px;
  --p-carousel-indicator-height: 12px;
  --p-carousel-indicator-list-gap: 5px;
  --p-carousel-indicator-border-radius: 0%;
  --p-carousel-indicator-background: #FFFFFF;
  --p-carousel-indicator-active-background: #F04E30;
}
</style>
