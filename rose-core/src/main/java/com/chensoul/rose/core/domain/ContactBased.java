/*
 * Copyright © 2025 Chensoul, Inc. (ichensoul@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chensoul.rose.core.domain;

import java.io.Serializable;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @param <I>
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class ContactBased<I extends Serializable> extends BaseDataWithExtra<I> implements HasEmail, HasId<I> {

    private static final long serialVersionUID = 5047448057830660988L;

    protected String phone;

    @Pattern(regexp = EMAIL_REGEXP, message = "邮箱不合法")
    protected String email;

    protected String country;

    protected String state;

    protected String city;

    protected String address;

    protected String zip;
}
