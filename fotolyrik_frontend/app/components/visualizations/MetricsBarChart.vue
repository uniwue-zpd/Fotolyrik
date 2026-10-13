<script setup lang="ts">
import * as d3 from "d3";

const props = defineProps<{
  data: Object;
  width?: number;
  height?: number;
  legend: Record<string, string>;
}>();

const legend = props.legend;
const svgRef = ref<SVGSVGElement | null>(null);
const containerRef = ref<HTMLDivElement | null>(null);

const keys = Object.keys(legend) as (keyof typeof legend)[];

function render() {
  if (!svgRef.value || !containerRef.value || !props.data) return;

  const containerWidth = containerRef.value.clientWidth || 700;
  const isMobile = containerWidth < 500;

  const width = props.width ?? 700;
  const height = isMobile
      ? keys.length * 60
      : (props.height ?? 200);

  const labelArea = isMobile ? 0 : 180;
  const valuePad = 8;

  d3.select(svgRef.value)
      .attr("viewBox", `0 0 ${width} ${height}`)
      .selectAll("*").remove();

  const color = d3.scaleOrdinal<string>()
      .domain(keys)
      .range(d3.schemeTableau10);

  const values = keys.map(k => (props.data as any)[k]);

  const y = d3.scaleBand()
      .domain(keys)
      .range([10, height - 10])
      .padding(isMobile ? 0.3 : 0.2);

  const x = d3.scaleLinear()
      .domain([0, d3.max(values) ?? 0])
      .nice()
      .range([labelArea, width - 40]);

  const svg = d3.select(svgRef.value);

  svg.selectAll("rect")
      .data(keys)
      .enter()
      .append("rect")
      .attr("x", labelArea)
      .attr("y", d => isMobile ? (y(d)! + 24) : y(d)!)
      .attr("height", d => isMobile ? 12 : y.bandwidth())
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
      .attr("width", d => Math.max(0, x((props.data as any)[d]) - labelArea));

  svg.selectAll("text.label")
      .data(keys)
      .enter()
      .append("text")
      .attr("x", 0)
      .attr("y", d => isMobile ? y(d)! : (y(d)! + y.bandwidth() / 2))
      .attr("dy", isMobile ? "1em" : "0.35em")
      .style("font-size", isMobile ? "18px" : "12px")
      .style("font-weight", isMobile ? "600" : "normal")
      .style("fill", "currentColor")
      .text(d => legend[d] ?? "");

  svg.selectAll("text.value")
      .data(keys)
      .enter()
      .append("text")
      .attr("class", "value")
      .attr("x", d => x((props.data as any)[d]) + valuePad)
      .attr("y", d => isMobile ? (y(d)! + 24 + 6) : (y(d)! + y.bandwidth() / 2))
      .attr("dy", "0.35em")
      .style("font-size", isMobile ? "14px" : "12px")
      .style("fill", "currentColor")
      .style("opacity", 0)
      .text(d => (props.data as any)[d]);
}

let resizeObserver: ResizeObserver | null = null;

onMounted(() => {
  render();

  if (containerRef.value) {
    resizeObserver = new ResizeObserver(() => render());
    resizeObserver.observe(containerRef.value);
  }
});

onUnmounted(() => {
  if (resizeObserver) resizeObserver.disconnect();
});

watch(
    () => props.data,
    render,
    { deep: true }
);
</script>

<template>
  <div ref="containerRef" class="chart-container">
    <svg ref="svgRef" preserveAspectRatio="xMinYMin meet" />
  </div>
</template>

<style scoped>
.chart-container {
  width: 100%;
}

svg {
  width: 100%;
  height: auto;
  display: block;
}
</style>
