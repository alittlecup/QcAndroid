package com.qingchengfit.fitcoach.di;

import cn.qingchengfit.saasbase.repository.ICardModel;
import com.qingchengfit.fitcoach.fragment.card.CardModel;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class AppAbstractModule {
  @Binds abstract ICardModel bindCardModel(CardModel cardModel);
}
