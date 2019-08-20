import {Node} from 'tiptap'
import ArticleImage from './ArticleImage.vue'

export default class Image extends Node {
  get name() {
    return 'image'
  }

  get schema() {
    return {
      attrs: {
        image: {
          default: null,
        },
        caption: {
          default: null,
        },
      },
      group: 'block',
      draggable: true,

      parseDOM: [{tag: 'figure',}],
      toDOM: node => ['figure', node.attrs],
    }
  }

  commands({type}) {
    return (attrs) => (state, dispatch) => {
      const node = type.create(attrs)
      dispatch(state.tr.replaceSelectionWith(node))
    }
  }

  get view() {
    return ArticleImage
  }
}
