import { rootClasses } from './formkit.theme'
import { defaultConfig } from '@formkit/vue'
import { createAutoHeightTextareaPlugin } from '@formkit/addons'

const formkitConfig = defaultConfig({
    plugins: [
        createAutoHeightTextareaPlugin(),
    ],
    config: {
        rootClasses,
    },
})

export default formkitConfig
