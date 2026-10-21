<script setup lang="ts">
import * as d3 from "d3";
import { ref, onMounted, onUnmounted, watch, computed } from "vue";
import type { PhotoPoemPublicationDateDTO } from "~/utils/types";

const props = withDefaults(defineProps<{
  width?: number;
  height?: number;
  data: PhotoPoemPublicationDateDTO[];
  fillColor?: string | ((d: PhotoPoemPublicationDateDTO) => string);
  enableZoom?: boolean;
}>(), {
  width: 1000,
  height: 200,
  fillColor: "#2563eb",
  enableZoom: false
});

const svgRef = ref<SVGSVGElement | null>(null);
const isMobile = ref(false);
const startYear = 1850;
const endYear = 1950;

const checkMobile = () => {
  if (typeof window !== "undefined") {
    isMobile.value = window.innerWidth <= 640;
  }
};

// Dynamically increase height on mobile to accommodate larger text without clipping
const currentHeight = computed(() => {
  return isMobile.value ? props.height + 50 : props.height;
});

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
  const chartHeight = currentHeight.value;
  const centerY = chartHeight / 2;
  const axisY = chartHeight - 30;

  const x = d3.scaleLinear()
      .domain([startYear, endYear])
      .range([60, props.width - 30]);

  // Render Axis with explicit classes and adjusted position for mobile text room
  svg.append("g")
      .attr("class", "axis x-axis")
      .attr("transform", `translate(0, ${axisY})`)
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
      y: centerY
    };
  });

  // Simulation
  const simulation = d3.forceSimulation(nodes as any)
      .force("x", d3.forceX((d: any) => d.fx).strength(1))
      .force("y", d3.forceY(centerY).strength(0.05))
      .force("collide", d3.forceCollide(7))
      .stop();

  for (let i = 0; i < 300; i++) {
    simulation.tick();
  }

  // Draw Circles
  const circles = svg.selectAll("circle.point")
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

  svg.selectAll("circle.point")
      .transition()
      .duration(1500)
      .delay((_, i) => i * 100)
      .attr("r", 5)
      .ease(d3.easeCubicOut);

  // Zoom management
  if (props.enableZoom) {
    const zoomBehavior = d3.zoom<SVGSVGElement, unknown>()
        .scaleExtent([0.2, 1.8])
        .on("zoom", (event) => {
          const { k, y } = event.transform;
          circles.attr("cy", (d: any) => d.y * k + y);
        });
    svg.call(zoomBehavior);
  }
}

onMounted(() => {
  checkMobile();
  window.addEventListener("resize", checkMobile);
  render();
});

onUnmounted(() => {
  window.removeEventListener("resize", checkMobile);
});

watch(() => props.data, render, { deep: true });
watch(currentHeight, () => {
  render();
});
</script>

<template>
  <div class="timeline-chart-wrapper">
    <slot name="extra" :svg="d3.select(svgRef)" />

    <svg ref="svgRef" class="timeline-d3-svg" :viewBox="`0 0 ${width} ${currentHeight}`"/>
  </div>
</template>

<style scoped>
.timeline-chart-wrapper {
  width: 100%;
  color: currentColor;
}

.timeline-d3-svg {
  width: 100%;
  height: auto;
  overflow: hidden; /* Keeps chart points cleanly clipped */
}
</style>

<style>
.timeline-d3-svg text,
.timeline-d3-svg .axis text,
.timeline-d3-svg .x-axis text {
  fill: currentColor;
  font-size: 13px;
  font-weight: 500;
}

.timeline-d3-svg line,
.timeline-d3-svg path.domain {
  stroke: currentColor;
}

@media (max-width: 640px) {
  .timeline-d3-svg text,
  .timeline-d3-svg .axis text,
  .timeline-d3-svg .x-axis text {
    font-size: 26px !important;
  }
}
</style>
