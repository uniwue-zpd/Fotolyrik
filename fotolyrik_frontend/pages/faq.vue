<script setup lang="ts">
</script>

<template>
  <div class="columns-1 w-full">
  <h1 class="text-3xl outfit-headline font-bold">FAQ</h1>
  <br>
  <div >
  <h2 class="text-2xl outfit-headline font-bold">Themenblock</h2>
  <br>
  <div id="accordion">
    <div class="card">
      <Accordion class="themed-accordion"  value="0" expandIcon="pi pi-plus" collapseIcon="pi pi-minus">
        <AccordionPanel value="0" >
          <AccordionHeader><div class="text-lg outfit-headline">Frage I</div></AccordionHeader>
          <AccordionContent>
            <p class="m-0">
              Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
              sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat,
              sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren,
              no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
              sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.
              At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren,
              no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet,
              consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat,
              sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum.
              Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.
            </p>
          </AccordionContent>
        </AccordionPanel>
        <AccordionPanel value="1">
          <AccordionHeader><div class="text-lg outfit-headline">Frage II</div></AccordionHeader>
          <AccordionContent>
            <p class="m-0">
              Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim
              ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Consectetur, adipisci velit, sed quia non numquam eius modi.
            </p>
          </AccordionContent>
        </AccordionPanel>
        <AccordionPanel value="2">
          <AccordionHeader><div class="text-lg outfit-headline">Frage III</div></AccordionHeader>
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
    <h2 class="text-2xl outfit-headline font-bold">Themenblock</h2>
  </div>
  </div>
</template>

<style scoped>
.themed-accordion {
  --p-accordion-header-color: black;
  --p-accordion-header-active-color: black;
  --p-accordion-header-hover-color: black;
  --p-accordion-header-active-background: #E8E8E8;
  --p-accordion-header-active-hover-background: #F0F0F0;
  --p-accordion-header-hover-background: #F0F0F0;
  --p-accordion-content-border-width: 1px;
  --p-accordion-content-border-color: #F0F0F0;
  --p-accordion-content-padding: 1%;
}
</style>
