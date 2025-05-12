<script setup lang="ts">
import { ref } from 'vue';
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
  <div class="flex flex-col gap-2">
    <img src= "https://unsplash.it/1200/200" alt=""/>
    <h1 class="text-3xl outfit-headline font-bold">Projekt</h1>
    <div class="intro columns-2">
      <div class="roboto-plain">
        Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod
        tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et
        accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est
        Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
        sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.
        At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata
        sanctus est Lorem ipsum dolor sit amet.
      </div>
    </div>
    <div class="progress flex flex-col gap-2">
      <h2 class="text-2xl outfit-headline font-bold">Fortschritt</h2>
      <Timeline :value="events" layout="vertical" align="alternate" class="items-center">
        <template #marker>
          <span class="flex w-8 h-8 items-center justify-center rounded-full border-2 border-[#063D79]">
            <i class="pi pi-calendar-clock" style="color: #063D79"/>
          </span>
        </template>
        <template #content="slotProps">
          <Card style="background-color: #F1F2F2" class="flex-1 mb-3">
            <template #title>
              <div class="text-lg outfit-headline">{{ slotProps.item.date }}</div>
            </template>
            <template #content>
              <div class="text-sm roboto-plain">
                {{ slotProps.item.text }}
              </div>
            </template>
          </Card>
        </template>
      </Timeline>
    </div>
    <div class="contributions flex flex-col gap-2">
      <h2 class="text-2xl outfit-headline font-bold">Forschungsbeiträge</h2>
      <ul class="list-disc list-inside">
        <li>Beitrag</li>
        <li>Beitrag</li>
        <li>Beitrag</li>
      </ul>
    </div>
    <div class="analytics flex flex-col gap-2">
      <h2 class="text-2xl outfit-headline font-bold">Fotolyrik in Zahlen</h2>
      <div class="flex flex-row space-x-2">
        <i class="pi pi-chart-bar"/>
        <div class="roboto-italic">Balkendiagramm mit Zahlen zur Fotolyrik</div>
      </div>
    </div>
    <div class="team flex flex-col gap-2">
      <h2 class="text-2xl outfit-headline font-bold">Ihre Ansprechpartner</h2>
      <div class="flex flex-row space-x-2">
        <i class="pi pi-user border-[#063D79] border-2 border-solid p-5"/>
        <div class="roboto-italic">Teammitglied</div>
      </div>
      <div class="flex flex-row space-x-2">
        <i class="pi pi-user border-[#063D79] border-2 border-solid p-5"/>
        <div class="roboto-italic">Teammitglied</div>
      </div>
      <div class="flex flex-row space-x-2">
        <i class="pi pi-user border-[#063D79] border-2 border-solid p-5"/>
        <div class="roboto-italic">Teammitglied</div>
      </div>
      <div class="flex flex-row space-x-2">
        <i class="pi pi-user border-[#063D79] border-2 border-solid p-5"/>
        <div class="roboto-italic">Teammitglied</div>
      </div>
      <div class="flex flex-row space-x-2">
        <i class="pi pi-user border-[#063D79] border-2 border-solid p-5"/>
        <div class="roboto-italic">Teammitglied</div>
      </div>
    </div>
    <div class="partner flex flex-col gap-2">
      <h2 class="text-2xl outfit-headline font-bold">Kooperationspartner</h2>
      <div class="flex flex-row justify-between">
        <i class="pi pi-users border-[#063D79] border-2 rounded-[50%] border-solid p-5"/>
        <i class="pi pi-users border-[#063D79] border-2 rounded-[50%] border-solid p-5"/>
        <i class="pi pi-users border-[#063D79] border-2 rounded-[50%] border-solid p-5"/>
        <i class="pi pi-users border-[#063D79] border-2 rounded-[50%] border-solid p-5"/>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>
