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

package com.vaadin.flow.component.menubar;

import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.function.SerializableRunnable;

public class MenuBarRootItem extends MenuItem {

    public MenuBarRootItem(SerializableRunnable contentReset) {
        super(null, contentReset);
    }

    @Override
    public void setCheckable(boolean checkable) {
        if (checkable) {
            throw new UnsupportedOperationException(
                    "A root level item in a MenuBar can not be checkable");
        }
    }

    // @Override
    // public ContextMenu getContextMenu() {
    // throw new IllegalStateException(
    // "This items belongs to a MenuBar instead of a ContextMenu");
    // // or just return null?
    // }
}
