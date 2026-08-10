<script setup lang="ts">
import Select from 'primevue/select';

const props = withDefaults(
    defineProps<{
      transformBeforeTrimming?: (val: any) => string;
      maxLen?: number;
    }>(),
    {
      transformBeforeTrimming: (val: any) => (val !== null && val !== undefined ? val : ''),
      maxLen: 21
    }
);

const truncate = (str: string, maxLen = props.maxLen) =>
    str.length > maxLen ? `${str.slice(0, maxLen).trimEnd()}...` : str;
</script>

<template>
  <Select v-bind="$attrs">
    <template v-for="(_, name) in $slots" #[name]="slotData">
      <slot v-if="name !== 'value'" :name="name" v-bind="slotData" />
    </template>

    <template #value="slotProps">
      <slot name="value" v-bind="slotProps">
        <div v-if="slotProps.value">
          {{ truncate(props.transformBeforeTrimming(slotProps.value), props.maxLen) }}
        </div>
        <span v-else>
          {{ slotProps.placeholder }}
        </span>
      </slot>
    </template>
  </Select>
</template>
