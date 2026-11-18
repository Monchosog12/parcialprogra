import { TestBed } from '@angular/core/testing';
import { Backservice } from './backservice';

describe('Backservice', () => {
  let service: Backservice;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Backservice);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
