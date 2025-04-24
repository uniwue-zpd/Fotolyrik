<script setup lang="ts">
function senden(values) {
  console.log(values);
}
</script>

<template>
  <div class="mb-2">
    <img src= "https://unsplash.it/1200/200" alt=""/>
  </div>
  <div class="grid place-content-center gap-2">
    <h1 class="text-3xl outfit-headline font-bold">Kontakt</h1>
    <div>
      <p>Hier können Sie Ihre Nachricht an uns hinterlassen.</p>
      <p>Füllen Sie bitte dafür die untenstehenden Felder aus</p>
      <p>und klicken Sie anschließend auf den Senden-Button.</p>
    </div>
    <FormKit type="form" @submit="senden" submit-label="Senden" #default ="{ value }" incomplete-message="Nicht alle Felder wurden ausfüllt.">
    <div class="flex flex-row gap-4">
      <div>
        <FormKit
            type="text"
            name="name"
            id="name"
            label="Name"
            placeholder="Name"
            validation="required"
            :validation-messages="{required: 'Bitte geben Sie einen Namen an.'}"
        />
      </div>
      <div>
        <FormKit
            type="email"
            label="Email"
            placeholder="Email"
            validation="required"
            :validation-messages="{
          required: 'Bitte geben Sie eine gültige E-Mail-Adresse an.'}"
        />
      </div>
    </div>
      <div class="flex flex-col">
        <div>
          <FormKit
              type="textarea"
              auto-height
              name="message"
              label="Nachricht"
              placeholder="Ihre Nachricht"
              validation="required"
              :validation-messages="{required: 'Bitte geben Sie eine Nachricht ein.'}"
          /> <!-- Größe von Textfeld ändern?-->
        </div>
        <div class="flex flex-col">
          <div>
            <FormKit
                type="checkbox"
                label="Ich habe die Datenschutzerklärung zur Kenntnis genommen."
                name="Datenschutzerklärung"
                decorator-icon="check"
                :value="false"
                validation="accepted"
                :validation-messages="{accepted: 'Datenschutzerklärung bitte bestätigen.'}"
            />
          </div>
        </div>
      </div>
    </FormKit>
  </div>
</template>

<style scoped>

</style>
