import {Node} from 'tiptap'
import ArticleAvatar from './ArticleAvatar.vue'

export default class Avatar extends Node {
  get name() {
    return 'avatar'
  }

  get schema() {
    return {
      attrs: {
        images: {
          default: [],
        },
        line1: {
          default: null,
        },
        line2: {
          default: null,
        }
      },
      group: 'block',
      draggable: true,

      parseDOM: [{tag: 'avatar',}],
      toDOM: node => ['avatar', node.attrs],
    }
  }

  commands({type}) {
    return (attrs) => (state, dispatch) => {
      const node = type.create(attrs)
      dispatch(state.tr.replaceSelectionWith(node))
    }
  }

  get view() {
    return ArticleAvatar
  }
}
