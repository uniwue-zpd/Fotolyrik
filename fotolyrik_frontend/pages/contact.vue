<script setup lang="ts">

</script>

<template>
    <div>
      <img src= "https://unsplash.it/1200/200" alt=""/>
  </div>
  <br>
  <div class="grid grid-flow-row auto-rows-max">
  <h1 class="text-3xl outfit-headline font-bold">Kontakt</h1>
  <br>
  <div>
    <p>Lorem ipsum dolor sit amet, consetetur</p>
    <p>sadipscing elitr, sed diam nonumy eirmod tempor</p>
    <p>invidunt ut labore et dolore magna Lorem ipsum</p>
    <p>dolor sit amet, consetetur sadipscing elitr, sed</p>
    <p>diam Lorem ipsum dolor sit amet, consetetur</p>
    <p>sadipscing elitr, sed diam.</p>
  </div>
    <br>
    <FormKit type="form" @submit="senden" submit-label="Senden" #default ="{ value }">
    <div class="flex flex-row gap-4">
      <div class="...">
    <FormKit
      type="text"
      name="name"
      id="name"
      label="Name"
      placeholder="Name"
    />
      </div>
      <div class="...">
        <FormKit
        type="email"
        label="Email"
        validation="required"
        validation-visibility="live"
        placeholder="Email">
        </FormKit>
      </div>
    </div>
  <div class="flex flex-col">
    <div class="...">
  <FormKit type="textarea"
           class="w-full"
           rows="2"
           name="message"
           label="Nachricht"
           placeholder="Ihre Nachricht"
           validation-visibility="live">
  </FormKit> <!-- Größe von Textfeld ändern?-->
  </div>
    <div class="flex flex-col">
    <div class="...">
      <FormKit
        type="checkbox"
        label="Ich habe die Datenschutzerklärung zur Kenntnis genommen."
        name="Datenschutzerklärung"
        :value="false"
        validation="required"
      > <!-- Farbe der Checkbox ändern? -->
      <pre wrap>{{ value }}</pre>
      </FormKit>
    </div>
    </div>
    </div>
    </FormKit>
    </div>
</template>

<style scoped>

</style>
