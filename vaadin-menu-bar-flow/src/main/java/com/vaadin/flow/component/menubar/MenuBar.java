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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.dom.Element;

/**
 * Server-side component for the <code>vaadin-menu-bar</code> element.
 *
 * @author Vaadin Ltd
 */
@Tag("vaadin-menu")
@HtmlImport("frontend://bower_components/vaadin-menu/src/vaadin-menu.html")
@JavaScript("frontend://contextMenuConnector.js")
@JavaScript("frontend://menuBarConnector.js")
public class MenuBar extends Component {

    private final List<RootMenuItem> items = new ArrayList<>();

    /**
     * 
     */
    public MenuBar() {
        container = new Element("div");
        getElement().appendVirtualChild(container);
        addAttachListener(event -> resetContent());
    }

    public RootMenuItem addItem(String text) {
        RootMenuItem rootMenuItem = new RootMenuItem();
        rootMenuItem.setText(text);
        items.add(rootMenuItem);
        getElement().appendChild(rootMenuItem.getContextMenu().getElement());
        return rootMenuItem;
    }

    private boolean updateScheduled = false;
    private final Element container;

    private void resetContent() {
        if (updateScheduled) {
            return;
        }
        updateScheduled = true;
        runBeforeClientResponse(ui -> {
            container.removeAllChildren();
            items.stream().map(item -> item.getElement())
                    .forEach(container::appendChild);

            int nodeId = container.getNode().getId();
            String appId = ui.getInternals().getAppId();

            ui.getPage().executeJavaScript(
                    "window.Vaadin.Flow.contextMenuConnector.generateItems($0, $1, $2)",
                    getElement(), appId, nodeId);

            IntStream.range(0, items.size()).forEach(i -> {
                ui.getPage().executeJavaScript(
                        "window.Vaadin.Flow.menuBarConnector.generateItems($0, $1, $2)",
                        getElement(), i,
                        getElement().getChildren()
                                .filter(child -> child.getTag()
                                        .equals("vaadin-context-menu"))
                                .collect(Collectors.toList()).get(i));
            });

            updateScheduled = false;
        });
    }

    private void resetContainers(RootMenuItem menuItem) {
        ContextMenu contextMenu = menuItem.getContextMenu();
        if (contextMenu.getChildren().count() == 0) {
            menuItem.getElement().removeProperty("_containerNodeId");
            return;
        }
    }
    //
    // private int createNewContainer(Stream<Component> components) {
    // Element subContainer = new Element("div");
    // container.appendChild(subContainer);
    //
    // components
    // .forEach(child -> subContainer.appendChild(child.getElement()));
    // return subContainer.getNode().getId();
    // }

    private void runBeforeClientResponse(Consumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }
}
