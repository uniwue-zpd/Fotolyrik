<script setup lang="ts">
import Chart from 'primevue/chart';

const chartData = ref();
const chartOptions = ref();
const photopoem_store = usePhotopoemStore();
const pubmedia_store = usePubMediumStore();
const place_store = usePlaceStore();
const person_store = usePersonStore();
const keyword_store = useKeywordStore();

const photopoemCount = computed(() => photopoem_store.photopoems.length);
const pubmediaCount = computed(() => pubmedia_store.pub_media.length);
const placeCount = computed(() => place_store.places.length);
const personCount = computed(() => person_store.persons.length);
const keywordCount = computed(() => keyword_store.keywords.length);

const setChartData = () => {
  return {
    labels: ['Fotogedichte', 'Publikationsmedien', 'Orte', 'Personen', 'Themen'],
    datasets: [
      {
        label: 'In der Sammlung',
        backgroundColor: ['#063D79', '#1E90FF', '#00C2A8', '#F6A400', '#6B7280'],
        hoverBackgroundColor: ['#1B5A9C', '#63B7FF', '#33D9C1', '#FFBF4D', '#9196A0'],
        data: [photopoemCount.value, pubmediaCount.value, placeCount.value, personCount.value, keywordCount.value]
      }
    ],
  };
};
const setChartOptions = () => {
  // ODO problem: this does not react if user changes the theme
  const darkModeTextColor = ()=>{return window.matchMedia('(prefers-color-scheme: dark)').matches? 'white':'black'};
  const textColor = darkModeTextColor();
  const textColorSecondary = darkModeTextColor();

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
          display: true,
          drawBorder: false
        }
      },
      y: {
        ticks: {
          color: textColorSecondary
        }
      }
    }
  };
}

onMounted(() => {
  chartData.value = setChartData();
  chartOptions.value = setChartOptions();
});
</script>

<template>
  <Chart type="bar" :data="chartData" :options="chartOptions" class="h-[20vh]"/>
</template>

<style scoped>

</style>
