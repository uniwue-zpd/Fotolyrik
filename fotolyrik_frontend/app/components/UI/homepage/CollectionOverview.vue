<script setup lang="ts">
import Chart from 'primevue/chart';

const chartData = ref();
const photopoem_api = usePhotopoem();
const pub_medium_api = usePubMedium();
const place_api = usePlace();
const person_api = usePerson();
const keyword_api = useKeyword();

const darkModeTextColor = computed(()=>{return useColorMode().value == 'dark'? 'white':'black'});


const {data: keywordList} = await keyword_api.useKeywordList();
const {data: personList} = await person_api.usePersonList();
const {data: photopoemList} = await photopoem_api.usePhotopoemList();
const {data: placeList} =  await place_api.usePlaceList();
const {data: pubMediumList} = await pub_medium_api.usePubMediumList();
const keywordCount = computed(()=> keywordList.value?.length);
const personCount = computed(()=> personList.value?.length);
const photopoemCount = computed(() => photopoemList.value?.length);
const placeCount = computed(() => placeList.value?.length);
const pubmediaCount = computed(() => pubMediumList.value?.length);


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
