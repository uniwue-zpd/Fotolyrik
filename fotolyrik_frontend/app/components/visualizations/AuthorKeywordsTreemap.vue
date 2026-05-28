<script setup lang="ts">
import * as d3 from "d3";

const props = defineProps<{
  data: KeywordCountDTO[];
  width?: number;
  height?: number;
}>();

type TreemapNode<T> = d3.HierarchyRectangularNode<T>;

const svgRef = ref<SVGSVGElement | null>(null);

const width = props.width ?? 800;
const height = props.height ?? 400;

function render() {
  if (!svgRef.value || !props.data?.length) return;

  d3.select(svgRef.value).selectAll("*").remove();

  const root = d3.hierarchy<KeywordCountDTO>({
    children: props.data
  } as any).sum(d => d.count) as TreemapNode<KeywordCountDTO>;

  d3.treemap<any>()
      .size([width, height])
      .padding(4)(root);

  const color = d3.scaleOrdinal<string>()
      .domain(props.data.map(d => d.keyword))
      .range(['#c9d6df', '#f0f0f0', '#dbe7e4', '#f7d9c4', '#d6ccc2']);

  const svg = d3.select(svgRef.value);

  const nodes = svg
      .selectAll("g")
      .data(root.leaves())
      .enter()
      .append("g")
      .attr("transform", d => `translate(${d.x0},${d.y0})`)
      .style("cursor", "pointer")
      .on("click", (_, d: any) => {
        navigateTo(`/keywords/${d.data.id}`);
      })
      .on("mouseover", function () {
        d3.select(this)
            .raise()
            .select("rect")
            .transition()
            .duration(150)
            .attr("transform", "scale(1.1)");
      })
      .on("mouseout", function () {
        d3.select(this)
            .select("rect")
            .transition()
            .duration(150)
            .attr("transform", "scale(1)");
      });

  nodes.append("rect")
      .attr("width", d => d.x1 - d.x0)
      .attr("height", d => d.y1 - d.y0)
      .attr("fill", d => color(d.data.keyword))
      .attr("rx", 5);

  nodes.append("text")
      .attr("x", 8)
      .attr("y", 20)
      .text(d => d.data.keyword)
      .style("font-size", "10px")
      .style("pointer-events", "none");

  nodes.append("text")
      .attr("x", 8)
      .attr("y", 30)
      .text(d => d.data.gndId)
      .style("font-size", "6px")
      .style("pointer-events", "none");

  nodes.append("text")
      .attr("x", 8)
      .attr("y", 40)
      .text(d => d.data.count)
      .style("font-size", "6px")
      .style("pointer-events", "none");
}

onMounted(() => {
  render();
});

watch(
    () => props.data,
    () => render(),
    { deep: true, immediate: true }
);
</script>

<template>
  <svg ref="svgRef" :viewBox="`0 0 ${width} ${height}`"/>
</template>

<style scoped>

</style>
