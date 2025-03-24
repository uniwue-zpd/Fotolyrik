<script setup lang="ts">
</script>

<template>
  <div class="columns-1 w-[800px]">
  <h1 class="text-4xl font-bold">FAQ</h1>
  <br>
  <div >
  <h1 class="text-4xl font-bold">Themenblock</h1>
  <br>
  <div id="accordion">
    <div class="card">
      <!-- Farbänderung mit Tailwind nicht möglich? -->
      <Accordion  value="0">
        <AccordionPanel class="bg-white" value="0" >
          <AccordionHeader>Frage I</AccordionHeader>
          <AccordionContent>
            <p class="m-0">
              Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo
              consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
            </p>
          </AccordionContent>
        </AccordionPanel>
        <AccordionPanel value="1">
          <AccordionHeader>Frage II</AccordionHeader>
          <AccordionContent>
            <p class="m-0">
              Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim
              ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Consectetur, adipisci velit, sed quia non numquam eius modi.
            </p>
          </AccordionContent>
        </AccordionPanel>
        <AccordionPanel value="2">
          <AccordionHeader>Frage III</AccordionHeader>
          <AccordionContent>
            <p class="m-0">
              At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa
              qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus.
            </p>
          </AccordionContent>
        </AccordionPanel>
      </Accordion>
    </div>
  </div>
    <br>
    <h1 class="text-4xl font-bold">Themenblock</h1>
  </div>
  </div>
</template>

<style scoped>

</style>
