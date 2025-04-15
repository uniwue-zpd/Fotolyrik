<script setup lang="ts">
function senden(values) {
  console.log(values);
}
</script>

<template>
    <div>
      <img src= "https://unsplash.it/1200/200" alt=""/>
  </div>
  <br>
  <div class="grid place-content-center">
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
    <FormKit type="form" @submit="senden" submit-label="Senden" #default ="{ value }" incomplete-message="Nicht alle Felder wurden ausfüllt.">
    <div class="flex flex-row gap-4">
      <div class="...">
    <FormKit
      type="text"
      name="name"
      id="name"
      label="Name"
      placeholder="Name"
      validation="required"
      :validation-messages="{
          required: 'Bitte geben Sie einen Namen an.'
        }"
    />
      </div>
      <div class="...">
        <FormKit
        type="email"
        label="Email"
        placeholder="Email"
        validation="required"
        :validation-messages="{
          required: 'Bitte geben Sie eine gültige E-Mail-Adresse an.'
        }">
        </FormKit>
      </div>
    </div>
  <div class="flex flex-col">
    <div class="...">
  <FormKit type="textarea"
           auto-height
           name="message"
           label="Nachricht"
           placeholder="Ihre Nachricht"
           validation="required"
           :validation-messages="{
            required: 'Bitte geben Sie eine Nachricht ein.'
           }">
  </FormKit> <!-- Größe von Textfeld ändern?-->
  </div>
    <div class="flex flex-col">
    <div class="...">
      <FormKit
        type="checkbox"
        label="Ich habe die Datenschutzerklärung zur Kenntnis genommen."
        name="Datenschutzerklärung"
        decorator-icon="check"
        :value="false"
        validation="accepted"
        :validation-messages="{
          accepted: 'Datenschutzerklärung bitte bestätigen.'
        }"
      >
      </FormKit>
    </div>
    </div>
    </div>
    </FormKit>
  </div>
</template>

<style scoped>

</style>
