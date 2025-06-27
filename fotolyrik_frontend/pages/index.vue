<script setup lang="ts">
// TODO: Define functions to parse data for the carousel and the bar chart

import Chart from 'primevue/chart';

import { ref, onMounted } from 'vue';

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

// start for bar chart example

onMounted(() => {
  chartData.value = setChartData();
  chartOptions.value = setChartOptions();
});

const chartData = ref();
const chartOptions = ref();

const setChartData = () => {
  const documentStyle = getComputedStyle(document.documentElement);

  return {
    labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
    datasets: [
      {
        label: 'My First dataset',
        backgroundColor: documentStyle.getPropertyValue('--p-cyan-500'),
        borderColor: documentStyle.getPropertyValue('--p-cyan-500'),
        data: [65, 59, 80, 81, 56, 55, 40]
      },
      {
        label: 'My Second dataset',
        backgroundColor: documentStyle.getPropertyValue('--p-gray-500'),
        borderColor: documentStyle.getPropertyValue('--p-gray-500'),
        data: [28, 48, 40, 19, 86, 27, 90]
      }
    ]
  };
};
const setChartOptions = () => {
  const documentStyle = getComputedStyle(document.documentElement);
  const textColor = documentStyle.getPropertyValue('--p-text-color');
  const textColorSecondary = documentStyle.getPropertyValue('--p-text-muted-color');
  const surfaceBorder = documentStyle.getPropertyValue('--p-content-border-color');

  return {
    indexAxis: 'y',
    maintainAspectRatio: false,
    aspectRatio: 0.8,
    plugins: {
      legend: {
        labels: {
          color: textColor
        }
      }
    },
    scales: {
      x: {
        ticks: {
          color: textColorSecondary,
          font: {
            weight: 500
          }
        },
        grid: {
          display: false,
          drawBorder: false
        }
      },
      y: {
        ticks: {
          color: textColorSecondary
        },
        grid: {
          color: surfaceBorder,
          drawBorder: false
        }
      }
    }
  };
};

// end of bar chart example
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
    <div class="intro py-5 px-[3.75rem]">
      <h1 class="text-3xl outfit-headline font-bold text-[#063D79]">
        Fotolyrik in illustrierten Zeitschriften zwischen 1895 und 1945
      </h1>
    </div>
    <div class="intro px-[3.75rem]">
      <div class="columns-2 space-y-8 gap-x-10 text-justify">
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
    <div class="py-5 px-[3.75rem]">
      <h1 class="text-3xl outfit-headline font-bold text-[#063D79]">Sammlung</h1>
    </div>
    <div class="collection bg-[#F1F2F2]">
      <div class="flex flex-col gap-2 px-[3.75rem] py-14">
        <div class="pb-3">
          <h2 class="text-3xl averia-layout italic text-[#063D79]">Highlight</h2>
        </div>
        <div class="flex flex-row space-x-5">
          <div class="basis-1/4 bg-white font-bold">Text placeholder</div>
          <div class="basis-3/4 font-bold">
            <img alt="placeholder image" src="../public/s-1.jpg"/>
          </div>
        </div>
      </div>
    </div>
    <div class="title py-5 px-[3.75rem]">
      <h2 class="text-3xl averia-layout italic text-[#063D79]">Fotolyrik in Zahlen</h2>
    </div>
    <div>
      <Chart type="bar" :data="chartData" :options="chartOptions" class="h-[30rem]" />
    </div>
    <div class="analytics px-[3.75rem]">
      <div class="flex flex-col gap-2">
        <div class="font-bold">
          Bar chart placeholder
          <i class="pi pi-chart-bar"/>
        </div>
        <div class="py-5">
        <div class="columns-2 space-y-8 gap-x-10 text-justify">
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
    <div class="title py-5 px-[3.75rem]">
      <h2 class="text-3xl averia-layout italic text-[#063D79]">Themen</h2>
    </div>
    <div class="topics bg-[#F1F2F2] p-5">
      <div class="flex flex-col gap-2">
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