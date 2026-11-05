export default defineAppConfig({
  ui: {
    colors: {
      primary: 'uniwue',
      neutral: 'zinc'
    },
    formField: {
      slots: {
        label: 'text-xs font-bold'
      }
    },
    accordion: {
      slots: {
        label: 'text-lg font-semibold outfit-headline'
      }
    },
    button: {
      slots: {
        base: 'roboto-plain'
      }
    }
  }
})
