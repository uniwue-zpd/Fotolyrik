<script setup lang="ts">
import Chart from 'primevue/chart';

const chartData = ref();
const photopoem_store = usePhotopoemStore();
const pubmedia_store = usePubMediumStore();
const place_store = usePlaceStore();
const person_api = usePerson();
const keyword_api = useKeyword();

const darkModeTextColor = computed(()=>{return useColorMode().value == 'dark'? 'white':'black'});
const photopoemCount = computed(() => photopoem_store.photopoems.length);
const pubmediaCount = computed(() => pubmedia_store.pub_media.length);
const placeCount = computed(() => place_store.places.length);

const { data: cachedKeywords } = await useAsyncData( 'keyword-list', () => keyword_api.fetchKeywords());
const keywordCount = computed(()=> cachedKeywords.value?.length);

const { data: cachedPersons } = await useAsyncData( 'person-list', () => person_api.fetchPersons());
const personCount = computed(()=> cachedPersons.value?.length);

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
const chartOptions = computed(() => {

  return {
    indexAxis: 'y',
    maintainAspectRatio: false,
    aspectRatio: 0.8,
    plugins: {
      legend: {
        labels: {
          color: darkModeTextColor.value
        }
      }
    },
    scales: {
      x: {
        ticks: {
          color: darkModeTextColor.value,
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
          color: darkModeTextColor.value
        }
      }
    }
  };
})

onMounted(() => {
  chartData.value = setChartData();
});
</script>

<template>
  <Chart type="bar" :data="chartData" :options="chartOptions" class="h-[20vh]"/>
</template>

<style scoped>

</style>
