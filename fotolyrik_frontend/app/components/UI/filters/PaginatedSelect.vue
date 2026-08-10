<script setup lang="ts" generic="TItem">
import Select, { type SelectProps } from 'primevue/select';

interface Props extends /* @vue-ignore */ SelectProps {
  fetchFunction: (query: string, pageable: Pageable) => Promise<Page<TItem>>;
  cullFunction: (item: TItem ) =>Partial<TItem>;
  pageSize?: number;
  sort: string;
}

const props = withDefaults(defineProps<Props>(), {
  cullFunction: (item: TItem)=>item,
  pageSize: 10,
  sort: ''
});

const options = ref<Partial<TItem> []>([]) as Ref<Partial<TItem>[]>;
const loading = ref(false);
const currentPage = ref(0);
const hasMore = ref(true);
const currentQuery = ref('');

const loadData = async (query: string, reset = true) => {
  if (loading.value && !reset) return;
  loading.value = true;

  try {
    if (reset) {
      currentPage.value = 0;
      options.value = [] as Partial<TItem>[];
    }

    const response = await props.fetchFunction(query, {
      page: currentPage.value,
      size: props.pageSize,
      sort: props.sort,
    });

    const newItems = response.content || [];
    const itemsCulled: Partial<TItem>[] = newItems.map(props.cullFunction)
    if (reset) {
      options.value = itemsCulled;
    } else {
      options.value = [...options.value, ...itemsCulled];
    }

    hasMore.value = currentPage.value + 1 < response.totalPages;
  } catch (error) {
    console.error('Failed to load paginated select options:', error);
  } finally {
    loading.value = false;
  }
};

const debouncedFetch = useDebounceFn((query: string) => {
  loadData(query, true);
}, 300);

const handleFilter = (event: { value: string }) => {
  currentQuery.value = event.value ?? '';
  loading.value = true;
  debouncedFetch(currentQuery.value);
};

const handleScroll = (event: Event) => {
  const target = event.target as HTMLElement;
  if (!target) return;

  const isBottom = target.scrollTop + target.clientHeight >= target.scrollHeight - 20;

  if (isBottom && hasMore.value && !loading.value) {
    currentPage.value++;
    loadData(currentQuery.value, false);
  }
};

onMounted(() => {
  loadData('', true);
});
</script>

<template>
  <Select
      :options="options"
      :loading="loading"
      filter
      @filter="handleFilter"
      :pt="{
        listContainer: {
          onScroll: handleScroll
        }
      }"
      v-bind="$attrs"
  >
    <template v-for="(_, name) in $slots" #[name]="slotProps">
      <slot :name="name" v-bind="slotProps || {}" />
    </template>
  </Select>
</template>
