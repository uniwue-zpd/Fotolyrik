<script setup lang="ts" generic="T extends PhotoPoemDTO">
import * as d3 from "d3";
import { ref, onMounted, watch } from "vue";

const props = withDefaults(defineProps<{
  width?: number;
  height?: number;
  data: T[];
  fillColor?: string | ((d: T) => string);
  enableZoom?: boolean;
}>(), {
  width: 1000,
  height: 200,
  fillColor: "#2563eb",
  enableZoom: false
});

const svgRef = ref<SVGSVGElement | null>(null);
const startYear = 1850;
const endYear = 1950;

function getYear(date?: string | null): number | null {
  if (!date) return null;
  const cleaned = String(date).trim().replace(/\s+/g, "");
  const match = cleaned.match(/(18|19|20)\d{2}/);
  return match ? Number(match[0]) : null;
}

function render() {
  if (!svgRef.value || !props.data?.length) return;

  // Clear previous elements
  d3.select(svgRef.value).selectAll("*").remove();

  const svg = d3.select(svgRef.value);

  // Conditionally append container if zoom is enabled (or always use it to match structural parity)
  const container = props.enableZoom
      ? svg.append("g").attr("class", "zoom-container")
      : svg;

  const x = d3.scaleLinear()
      .domain([startYear, endYear])
      .range([60, props.width - 30]);

  // Render Axis
  container.append("g")
      .attr("transform", `translate(0, ${props.height - 20})`)
      .call(
          d3.axisBottom(x)
              .tickFormat(d3.format("d"))
              .ticks(8)
      );

  // Map Nodes
  const nodes = props.data.map(d => {
    const year = getYear(d.publicationDate);
    const calculatedX = year ? x(year) : -10;
    return {
      ...d,
      fx: calculatedX,
      x: calculatedX,
      y: props.height / 2
    };
  });

  // Simulation
  const simulation = d3.forceSimulation(nodes as any)
      .force("x", d3.forceX((d: any) => d.fx).strength(1))
      .force("y", d3.forceY(props.height / 2).strength(0.05))
      .force("collide", d3.forceCollide(7))
      .stop();

  for (let i = 0; i < 300; i++) {
    simulation.tick();
  }

  // Draw Circles
  const circles = container.selectAll("circle.point")
      .data(nodes)
      .enter()
      .append("circle")
      .attr("class", "point")
      .attr("cx", (d: any) => d.x)
      .attr("cy", (d: any) => d.y)
      .attr("r", 0)
      .attr("fill", (d: any) => typeof props.fillColor === 'function' ? props.fillColor(d) : props.fillColor)
      .style("cursor", "pointer")
      .on("mouseover", function () {
        d3.select(this).raise().transition().duration(120).attr("r", 7);
      })
      .on("mouseout", function () {
        d3.select(this).transition().duration(120).attr("r", 5);
      })
      .on("click", (_, d: any) => {
        navigateTo(`/photopoems/${d.id}`);
      });

  circles.append("title")
      .text((d: any) => `${d.title ?? d.altTitle ?? 'Kein Titel'}`);

  container.selectAll("circle.point")
      .transition()
      .duration(1500)
      .delay((_, i) => i * 100)
      .attr("r", 5)
      .ease(d3.easeCubicOut);

  // Zoom management
  if (props.enableZoom) {
    const zoomBehavior = d3.zoom<SVGSVGElement, unknown>()
        .scaleExtent([0.3, 1.5])
        .on("zoom", (event) => {
          const { k, y } = event.transform;
          circles.attr("cy", (d: any) => d.y * k + y);
        });
    svg.call(zoomBehavior);
  }
}

onMounted(render);
watch(() => props.data, render, { deep: true });
</script>

<template>
  <div class="timeline-chart-wrapper">
    <slot name="extra" :svg="d3.select(svgRef)" />

    <svg ref="svgRef" :viewBox="`0 0 ${width} ${height}`"/>
  </div>
</template>
