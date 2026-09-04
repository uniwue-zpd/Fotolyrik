<script setup lang="ts">
import { ref, watch, onUnmounted } from 'vue'
import * as d3 from 'd3'

const props = defineProps<{
  id?: number
}>()

const person_api = usePerson()
const { data: graph } = await useAsyncData('person-worked-with-graph', person_api.fetchWorkedWithGraph)

const svgRef = ref<SVGSVGElement | null>(null)
let simulation: d3.Simulation<any, any> | null = null

function drawGraph() {
  if (!svgRef.value || !graph.value || !props.id || !graph.value.nodes[props.id]) return

  d3.select(svgRef.value).selectAll('*').remove()
  if (simulation) simulation.stop()

  const rawNodes = graph.value.nodes
  const rawEdges = graph.value.edges

  const nodes = Object.entries(rawNodes).map(([nodeId, name]) => ({
    id: Number(nodeId),
    name: name as string
  }))

  const nodeIds = new Set(Object.keys(rawNodes).map(Number))

  const links: { source: number; target: number }[] = []

  Object.entries(rawEdges).forEach(([sourceId, targetIds]) => {
    const s = Number(sourceId)
    if (!nodeIds.has(s)) return;
    (targetIds as number[]).forEach((t) => {
      if (nodeIds.has(t)) {
        links.push({ source: s, target: t })
      }
    })
  })

  let activeFocusId = props.id

  const width = 600
  const height = 400

  const svg = d3.select(svgRef.value)
      .attr('viewBox', `0 0 ${width} ${height}`)

  const g = svg.append('g')

  let isUserInteracting = false

  const zoom = d3.zoom<SVGSVGElement, unknown>()
      .scaleExtent([0.1, 8])
      .on('zoom', (event) => {
        g.attr('transform', event.transform)
        if (event.sourceEvent) {
          isUserInteracting = true
        }
      })

  svg.call(zoom as any)

  simulation = d3.forceSimulation(nodes as any)
      .force('link', d3.forceLink(links).id((d: any) => d.id).distance(100))
      .force('charge', d3.forceManyBody().strength(-20))
      .force('center', d3.forceCenter(width / 2, height / 2))

  const linkSelection = g.append('g')
      .attr('stroke', '#999')
      .selectAll('line')
      .data(links)
      .join('line')
      .attr('stroke-width', 2)

  const nodeSelection = g.append('g')
      .selectAll('g')
      .data(nodes)
      .join('g')
      .call(
          d3.drag<SVGGElement, any>()
              .on('start', (event, d) => {
                isUserInteracting = true
                if (!event.active) simulation?.alphaTarget(0.1).restart()
                d.fx = d.x
                d.fy = d.y
              })
              .on('drag', (event, d) => {
                d.fx = event.x
                d.fy = event.y
              })
              .on('end', (event, d) => {
                if (!event.active) simulation?.alphaTarget(0)
                d.fx = null
                d.fy = null
              }) as any
      )

  nodeSelection.append('circle')
      .attr('r', 12)
      .attr('fill', (d: any) => d.id === props.id ? '#e63946' : '#42b883')

  nodeSelection.append('text')
      .text((d: any) => d.name)
      .attr('x', 15)
      .attr('y', 4)
      .attr('font-size', '12px')
      .attr('fill', 'currentColor')

  function updateOpacity() {
    const activeNeighbors = new Set<number>([activeFocusId])
    links.forEach((l) => {
      const s = typeof l.source === 'object' ? (l.source as any).id : l.source
      const t = typeof l.target === 'object' ? (l.target as any).id : l.target
      if (s === activeFocusId) activeNeighbors.add(t)
      if (t === activeFocusId) activeNeighbors.add(s)
    })

    nodeSelection.attr('opacity', (d: any) => activeNeighbors.has(d.id) ? 1 : 0.25)

    linkSelection.attr('stroke-opacity', (d: any) => {
      const s = typeof d.source === 'object' ? d.source.id : d.source
      const t = typeof d.target === 'object' ? d.target.id : d.target
      return s === activeFocusId || t === activeFocusId ? 0.8 : 0.15
    })
  }

  nodeSelection.on('mouseenter', (_, d: any) => {
    activeFocusId = d.id
    updateOpacity()
  })

  updateOpacity()

  const targetNode = nodes.find((n) => n.id === props.id)

  simulation.on('tick', () => {
    linkSelection
        .attr('x1', (d: any) => d.source.x)
        .attr('y1', (d: any) => d.source.y)
        .attr('x2', (d: any) => d.target.x)
        .attr('y2', (d: any) => d.target.y)

    nodeSelection.attr('transform', (d: any) => `translate(${d.x},${d.y})`)

    if (targetNode && !isUserInteracting && (targetNode as any).x !== undefined) {
      const transform = d3.zoomIdentity
          .translate(width / 2, height / 2)
          .translate(-(targetNode as any).x, -(targetNode as any).y)
      svg.call(zoom.transform as any, transform)
    }
  })
}

watch([() => graph.value, () => props.id, svgRef], () => {
  drawGraph()
}, { flush: 'post' })

onUnmounted(() => {
  if (simulation) simulation.stop()
})
</script>

<template>
  <div v-if="props.id && graph?.nodes && graph.nodes[props.id] !== undefined">
    <Divider />
    <h2 class="text-xl font-bold text-primary outfit-headline">Kollaboriert mit:</h2>
    <svg ref="svgRef" class="w-full h-auto max-h-[500px] cursor-grab active:cursor-grabbing"></svg>
  </div>
</template>
