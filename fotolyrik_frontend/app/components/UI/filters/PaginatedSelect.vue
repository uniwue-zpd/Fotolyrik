<script setup lang="ts" generic="TItem, TValue extends string | number">
const props = withDefaults(defineProps<{
  fetchFunction: (query: string, pageable: Pageable) => Promise<Page<TItem>>;
  getValue: (item: TItem) => TValue | null | undefined;
  getLabel: (item: TItem) => string | null | undefined;
  pageSize?: number;
  sort: string;
  id: string;
  placeholder: string;
}>(), {
  pageSize: 10,
});

const model = defineModel<TValue | undefined>();
const options = shallowRef<{ label: string; value: TValue }[]>([]);
const searchTerm = ref('');
const loading = ref(false);
const page = ref(0);
const hasMore = ref(true);
const selectedLabel = ref('');
const menu = ref<{ viewportRef?: HTMLElement | null } | null>(null);
const optionKey = (value: string | number) => `${typeof value}:${value}`;
const menuItems = computed(() => options.value.map((item) => ({
  label: item.label,
  value: optionKey(item.value),
})));
const modelKey = computed(() => model.value == null ? undefined : optionKey(model.value));
let requestId = 0;
let searchTimer: ReturnType<typeof setTimeout> | undefined;

async function loadData(query: string, nextPage: number, id: number) {
  loading.value = true;
  try {
    const response = await props.fetchFunction(query, {
      page: nextPage,
      size: props.pageSize,
      sort: props.sort,
    });
    if (id !== requestId) return;

    const items = response.content.flatMap((item) => {
      const value = props.getValue(item);
      return value == null ? [] : [{ value, label: props.getLabel(item) ?? '' }];
    });
    options.value = nextPage === 0 ? items : [...options.value, ...items];
    page.value = nextPage;
    hasMore.value = nextPage + 1 < response.totalPages;
    const selected = options.value.find((item) => item.value === model.value);
    if (selected) selectedLabel.value = selected.label;
  } catch (error) {
    if (id === requestId) console.error('Failed to load paginated select options:', error);
  } finally {
    if (id === requestId) loading.value = false;
  }
}

watch(searchTerm, (query) => {
  const id = ++requestId;
  clearTimeout(searchTimer);
  options.value = [];
  hasMore.value = false;
  loading.value = true;
  searchTimer = setTimeout(() => loadData(query, 0, id), 300);
});

function updateSelection(key: string | null | undefined) {
  if (key == null) {
    model.value = undefined;
    return;
  }
  const selected = options.value.find((item) => optionKey(item.value) === key);
  if (!selected) {
    console.error('Selected option is no longer available:', key);
    return;
  }
  selectedLabel.value = selected.label;
  model.value = selected.value;
}

watch(model, (value) => {
  if (value == null) selectedLabel.value = '';
  else {
    const selected = options.value.find((item) => item.value === value);
    if (selected) selectedLabel.value = selected.label;
  }
});

useEventListener(() => menu.value?.viewportRef, 'scroll', (event: Event) => {
  const target = event.target as HTMLElement;
  if (target.scrollTop + target.clientHeight >= target.scrollHeight - 20 && hasMore.value && !loading.value) {
    loadData(searchTerm.value, page.value + 1, requestId);
  }
});

onMounted(() => loadData('', 0, ++requestId));
onUnmounted(() => {
  ++requestId;
  clearTimeout(searchTimer);
});
</script>

<template>
  <USelectMenu
    ref="menu"
    :id="id"
    :model-value="modelKey"
    v-model:search-term="searchTerm"
    :items="menuItems"
    value-key="value"
    :placeholder="placeholder"
    :loading="loading"
    :search-input="{ placeholder: 'Suchen...' }"
    ignore-filter
    clear
    class="w-full"
    @update:model-value="updateSelection"
  >
    <span v-if="model != null">{{ selectedLabel }}</span>
    <span v-else>{{ placeholder }}</span>
  </USelectMenu>
</template>
