<script setup lang="ts">
import * as d3 from "d3";
import type { ContributorRole } from "~/utils/types";

const props = defineProps<{
  width?: number;
  height?: number;
  data: PersonContributions[];
}>();

interface PersonContributions extends PhotoPoemDTO {
  role: ContributorRole;
}

const svgRef = ref<SVGSVGElement | null>(null);

const width = props.width ?? 1000;
const height = props.width ?? 400;

const startYear = 1850;
const endYear = 1950;

const colors: Record<ContributorRole, string> = {
  author: '#2563eb',
  photographer: '#dc2626',
  contributor: '#16a34a',
  depicted: '#9333ea'
};

const rolesMap: Record<ContributorRole, string> = {
  author: 'Autor:in',
  photographer: 'Fotograf:in',
  contributor: 'Mitgewirkt an',
  depicted: 'Abgebildet auf'
}

function getYear(date?: string | null): number | null {
  if (!date) return null;
  const cleaned = String(date).trim().replace(/\s+/g, "");
  const match = cleaned.match(/(18|19|20)\d{2}/);
  return match ? Number(match[0]) : null;
}

function render() {
  if (!svgRef.value || !props.data?.length) return;

  d3.select(svgRef.value).selectAll("*").remove();

  const svg = d3.select(svgRef.value);

  const x = d3.scaleLinear()
      .domain([startYear, endYear])
      .range([60, width - 30]);

  svg.append("g")
      .attr("transform", `translate(0, ${height - 20})`)
      .call(
          d3.axisBottom(x)
              .tickFormat(d3.format("d"))
              .ticks(8)
      );

  const nodes = props.data.map(d => ({
    ...d,
    fx: (() => {
      const year = getYear(d.publicationDate);
      return year ? x(year) : -10;
    })(),
    x: (() => {
      const year = getYear(d.publicationDate);
      return year ? x(year) : -10;
    })(),
    y: height / 2
  }));

  const simulation = d3.forceSimulation(nodes as any)
      .force(
          "x",
          d3.forceX((d: any) => d.fx).strength(1)
      )
      .force(
          "y",
          d3.forceY(height / 2).strength(0.05)
      )
      .force(
          "collide",
          d3.forceCollide(7)
      )
      .stop();

  for (let i = 0; i < 300; i++) {
    simulation.tick();
  }

  svg.selectAll("circle.point")
      .data(nodes)
      .enter()
      .append("circle")
      .attr("class", "point")
      .attr("cx", (d: any) => d.x)
      .attr("cy", (d: any) => d.y)
      .attr("r", 0)
      .attr("fill", d => colors[d.role])
      .style("cursor", "pointer")
      .on("mouseover", function () {
        d3.select(this)
            .raise()
            .transition()
            .duration(120)
            .attr("r", 7);
      })
      .on("mouseout", function () {
        d3.select(this)
            .transition()
            .duration(120)
            .attr("r", 5);
      })
      .on("click", (_, d) => {
        navigateTo(`/photopoems/${d.id}`);
      })
      .append("title")
      .text(d => `${d.title ?? d.altTitle ?? 'Kein Titel'}`);

  svg.selectAll("circle.point")
      .transition()
      .duration(1500)
      .delay((_, i) => i * 100)
      .attr("r", 5)
      .ease(d3.easeCubicOut);

  // LEGEND
  const legend = svg.append("g")
      .attr("transform", "translate(60, 10)");

  Object.entries(colors).forEach(([role, color], i) => {
    const g = legend.append("g")
        .attr("transform", `translate(${i * 140}, 0)`);

    g.append("circle")
        .attr("r", 5)
        .attr("fill", color);

    g.append("text")
        .attr("x", 10)
        .attr("y", 4)
        .text(rolesMap[role as ContributorRole])
        .style("font-size", "12px")
        .style("fill", "#333");
  });
}

onMounted(() => {
  render();
});

watch(
    () => props.data,
    () => render(),
    { deep: true }
);
</script>

<template>
  <svg ref="svgRef" :viewBox="`0 0 ${width} ${height}`"/>
</template>

<style scoped>
</style>
