<script setup lang="ts">
import * as d3 from "d3";
import type {PersonMetricsDTO, PubMediumMetricsDTO} from "~/utils/types";

const props = defineProps<{
  data: PubMediumMetricsDTO;
  width?: number;
  height?: number;
}>();

const legend = {
  photopoems: "Fotogedichte",
  authorsWorkedWith: "Gearbeitet mit Autor:innen",
  photographersWorkedWith: "Gearbeitet mit Fotograf:innen",
  depictedPeople: "Abgebildete Personen",
  keywords: "Schlagworte"
};

const svgRef = ref<SVGSVGElement | null>(null);

const width = props.width ?? 700;
const height = props.height ?? 200;


const keys = Object.keys(legend) as (keyof typeof legend)[];

function render() {
  if (!svgRef.value || !props.data) return;
  d3.select(svgRef.value).selectAll("*").remove();

  const color = d3.scaleOrdinal<string>()
      .domain(keys)
      .range(d3.schemeTableau10);
  const values = keys.map(k => (props.data as any)[k]);

  const labelArea = 200;
  const valuePad = 5;

  const y = d3.scaleBand()
      .domain(keys)
      .range([20, height - 20])
      .padding(0.1);

  const x = d3.scaleLinear()
      .domain([0, d3.max(values) ?? 0])
      .nice()
      .range([labelArea, width - 20]);

  const svg = d3.select(svgRef.value);

  svg.selectAll("rect")
      .data(keys)
      .enter()
      .append("rect")
      .attr("x", labelArea)
      .attr("y", d => y(d)!)
      .attr("height", y.bandwidth())
      .attr("fill", d => color(d))
      .attr("opacity", 1)
      .style("cursor", "pointer")
      .attr("width", 0)
      .on("mouseover", function (_, d) {
        d3.select(this).attr("opacity", 0.7);
        d3.select(svgRef.value!)
            .selectAll("text.value")
            .filter(v => v === d)
            .style("opacity", 1);
      })
      .on("mouseout", function (_, d) {
        d3.select(this).attr("opacity", 1);
        d3.select(svgRef.value!)
            .selectAll("text.value")
            .filter(v => v === d)
            .style("opacity", 0);
      })
      .transition()
      .duration(1000)
      .delay((_, i) => i * 100)
      .attr("width", d => x((props.data as any)[d]) - labelArea);

  svg.selectAll("text.label")
      .data(keys)
      .enter()
      .append("text")
      .attr("x", 10)
      .attr("y", d => (y(d)! + y.bandwidth() / 2))
      .attr("dy", "0.35em")
      .style("font-size", "12px")
      .attr("class", "roboto-plain")
      .style("fill", "#333")
      .text(d => legend[d]);

  svg.selectAll("text.value")
      .data(keys)
      .enter()
      .append("text")
      .attr("class", "value")
      .attr("x", d => x((props.data as any)[d]) + valuePad)
      .attr("y", d => (y(d)! + y.bandwidth() / 2))
      .attr("dy", "0.35em")
      .style("font-size", "12px")
      .style("fill", "#444")
      .style("opacity", 0)
      .text(d => (props.data as any)[d]);
}

onMounted(async () => {
  render();
});

watch(
    () => props.data,
    render,
    { deep: true }
);
</script>

<template>
  <svg ref="svgRef" :viewBox="`0 0 ${width} ${height}`"/>
</template>

<style scoped>

</style>
