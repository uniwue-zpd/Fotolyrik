<script setup lang="ts">
const { loggedIn, user, clear } = useUserSession();

const logout = async () => {
  await $fetch('/auth/logout', {
    method: 'POST',
  });

  await clear();
};

const initial = computed(() => {
  return user.value?.name?.charAt(0).toUpperCase() ?? '?';
});
</script>

<template>
  <div class="min-w-[220px]">
    <template v-if="loggedIn">
      <div class="flex items-center gap-3 mb-4">
        <div class="relative">
          <div class="w-12 h-12 rounded-full bg-primary text-white flex items-center justify-center font-semibold text-lg">
            {{ initial }}
          </div>
          <span class="absolute top-0 right-0 w-3 h-3 rounded-full bg-green-500 border-2 border-white"/>
        </div>
        <div>
          <div class="font-semibold">{{ user?.name }}</div>
          <div class="text-sm text-gray-500">Online</div>
        </div>
      </div>
      <Button
          label="Abmelden"
          icon="pi pi-sign-out"
          severity="secondary"
          class="w-full"
          @click="logout"
      />
    </template>
    <template v-else>
      <div class="flex flex-col gap-2">
        <div class="text-sm">Sie sind nicht angemeldet</div>
        <a
            href="/auth/login"
            class="w-full flex items-center justify-center gap-2 p-1.5 rounded-md bg-primary text-white hover:bg-primary-400 transition font-medium cursor-pointer shadow-md hover:shadow-lg"
        >
          <i class="pi pi-sign-in"/>
          <span>Anmelden</span>
        </a>
      </div>
    </template>
  </div>
</template>