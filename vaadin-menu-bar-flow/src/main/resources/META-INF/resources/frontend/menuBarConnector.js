/*
 * Copyright 2000-2019 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

window.Vaadin.Flow.menuBarConnector = {

  generateItems: function (menuBar, i, contextMenu) {
    requestAnimationFrame(() => this._generateItems(menuBar, i, contextMenu));
  },

  _generateItems: function (menuBar, i, contextMenu) {

    // for (var i = 0; i < menuBar.items.length; i++) {
    menuBar.shadowRoot.querySelectorAll('vaadin-menu-root-button')[i].appendChild(menuBar.items[i].component);
    // }

    menuBar.items[i].children = contextMenu.items;
    // menuBar.items.forEach(item => {
    //   const contextMenu = item.component.querySelector('vaadin-context-menu');
    //   item.children = contextMenu.items;
    // });
    menuBar.notifyPath('items.' + i + '.children');
  }
}
