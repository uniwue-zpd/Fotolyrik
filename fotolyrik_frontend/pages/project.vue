<script setup lang="ts">
import { ref } from 'vue';
import CollectionOverview from "~/components/UI/homepage/CollectionOverview.vue";
// Placeholder data
const events = ref([
  {date: '01.01.2023', text: 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod'},
  {date: '03.06.2024', text: 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod'},
  {date: '04.05.2025', text: 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod'},
  {date: '05.06.2026', text: 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod'},
  {date: '01.03.2027', text: 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod'},
]);
</script>

<template>
  <div class="flex flex-col gap-4">
    <img src= "https://unsplash.it/1200/200" alt="Placeholder image"/>
    <div class="flex flex-col gap-4">
      <div class="flex flex-col gap-4 px-5 md:px-15">
        <h1 class="text-3xl text-[#063D79] outfit-headline font-bold">Fotolyrik in illustrierten Zeitschriften zwischen 1895 und 1945</h1>
        <div class="columns-2">
          <div class="roboto-plain text-justify mb-2">
            Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod
            tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et
            accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est
            Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
            sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.
            At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata
            sanctus est Lorem ipsum dolor sit amet.
          </div>
          <div class="flex flex-row justify-center">
            <NuxtLink to="/photopoems" class="averia-layout font-semibold text-sm text-[#F04E30] border-2 border-[#F04E30]  p-1">
              zur Sammlung
            </NuxtLink>
          </div>
        </div>
      </div>
      <div class="flex flex-col gap-4 px-5 md:px-15">
        <h2 class="text-xl averia-layout font-bold italic text-[#063D79]">Projektfortschritt</h2>
        <Timeline :value="events" layout="vertical" align="alternate" class="items-center">
          <template #marker>
          <span class="flex w-8 h-8 items-center justify-center rounded-full border-2 border-[#063D79]">
            <i class="pi pi-calendar-clock" style="color: #063D79"/>
          </span>
          </template>
          <template #content="slotProps">
            <div class="flex flex-col gap-2 bg-[#F1F2F2] rounded-md shadow-md p-2">
              <div class="text-lg outfit-headline font-semibold">{{ slotProps.item.date }}</div>
              <div class="text-sm roboto-plain">
                {{ slotProps.item.text }}
              </div>
            </div>
          </template>
        </Timeline>
      </div>
      <div class="flex flex-col gap-4">
        <h2 class="text-2xl outfit-headline font-bold text-[#063D79] px-5 md:px-15">Forschungsbeiträge</h2>
        <div class="bg-[#F1F2F2]">
          <div class="flex flex-col gap-2 py-5 px-5 md:px-15">
            <h2 class="text-xl averia-layout font-bold italic text-[#063D79]">Artikelübersicht</h2>
            <ul class="list-disc list-inside robot-plain">
              <li>Artikel XYZ</li>
              <li>Artikel XYZ</li>
              <li>Artikel XYZ</li>
            </ul>
          </div>
        </div>
      </div>
      <div class="flex flex-col gap-4 px-5 md:px-15">
        <h2 class="text-2xl outfit-headline font-bold text-[#063D79]">Fotolyrik in Zahlen</h2>
        <CollectionOverview/>
      </div>
      <div class="flex flex-col gap-4">
        <h2 class="text-2xl outfit-headline font-bold text-[#063D79] px-5 md:px-15">Kooperationspartner</h2>
        <div class="bg-[#F1F2F2]">
          <div class="grid grid-cols-3 md:grid-cols-5 gap-4 justify-items-center px-5 md:px-15 py-5">
            <div class="w-[100px] h-[100px] bg-[#063D79]"/>
            <div class="w-[100px] h-[100px] bg-[#063D79]"/>
            <div class="w-[100px] h-[100px] bg-[#063D79]"/>
            <div class="w-[100px] h-[100px] bg-[#063D79]"/>
            <div class="w-[100px] h-[100px] bg-[#063D79]"/>
            <div class="w-[100px] h-[100px] bg-[#063D79]"/>
            <div class="w-[100px] h-[100px] bg-[#063D79]"/>
            <div class="w-[100px] h-[100px] bg-[#063D79]"/>
            <div class="w-[100px] h-[100px] bg-[#063D79]"/>
            <div class="w-[100px] h-[100px] bg-[#063D79]"/>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>
