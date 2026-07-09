<script setup lang="ts">

</script>

<template>
  <div class="mx-auto mt-16 max-w-3xl">
    <div class="overflow-hidden rounded-2xl border border-gray-200 bg-white shadow-xl">
      <div class="flex flex-col items-center gap-6 p-10">
        <div class="flex h-28 w-28 items-center justify-center rounded-full ring-4 ring-primary">
          <Icon name="material-symbols:encrypted-outline-rounded" class="text-7xl text-primary"/>
        </div>
        <h1 class="outfit-headline text-4xl font-bold text-primary">
          Geschützter Bereich
        </h1>
        <p class="max-w-xl text-center text-lg text-gray-600 roboto-plain">
          Dieser Bereich ist ausschließlich für angemeldete Benutzer zugänglich.
        </p>
        <div class="w-full max-w-xl rounded-2xl border border-gray-200 bg-gray-50 p-5 text-center">
          <p class="text-gray-700 roboto-plain">
            Melden Sie sich mit Ihrem Fotolyrik-Konto an, um auf diese Seite
            zugreifen und alle Funktionen nutzen zu können.
          </p>
        </div>
        <NuxtLink
            to="/auth/login"
            external
            class="inline-flex items-center gap-2 rounded-xl bg-primary p-2.5 text-lg font-semibold text-white shadow-md transition-all duration-200 hover:-translate-y-0.5 hover:shadow-lg"
        >
          <Icon name="material-symbols:login-rounded" class="text-2xl"/>
          <span class="roboto-plain">Anmelden</span>
        </NuxtLink>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>
